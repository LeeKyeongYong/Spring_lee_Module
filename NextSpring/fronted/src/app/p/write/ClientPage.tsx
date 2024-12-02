"use client";

import { useAuth } from "@/hooks/useAuth";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function ClientPage() {
    const { getAccessToken, isLoggedIn, isInitialized } = useAuth();
    const router = useRouter();

    // 로그인 상태 확인
    useEffect(() => {
        if (isInitialized && !isLoggedIn()) {
            alert("로그인이 필요합니다.");
            router.push("/login");
        }
    }, [isInitialized, isLoggedIn, router]);

    // 로그인 여부 확인 후 렌더링
    if (!isInitialized || !isLoggedIn()) {
        return <div>Loading...</div>; // 초기 상태에서는 로딩 표시
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const title = formData.get("title") as string;
        const body = formData.get("body") as string;
        const postData = { title, body, published: true, listed: true }; // 필드 이름 수정
        const apiUrl = `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}`;
        try {
            const accessToken = await getAccessToken(); // 토큰을 여기서만 호출
            console.log('Access Token:', accessToken); // 토큰 값 확인
            const response = await fetch(apiUrl+'/posts', { // URL 수정
                method: "POST",
                body: JSON.stringify(postData),
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${accessToken}`,
                },
            });

            if (!response.ok) {
                console.log('Response status:', response.status); // 응답 상태 확인
                throw new Error(`HTTP error ${response.status}`);
            }

            alert("등록되었습니다.");
            router.back();
        } catch (error) {
            console.error("Failed to create post:", error);
            alert("등록에 실패했습니다. 다시 시도해주세요.");
        }
    };

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목" required /> {/* 필수 입력 필드 추가 */}
                <textarea name="body" placeholder="내용" required /> {/* 필수 입력 필드 추가 */}
                <button type="submit">등록</button>
            </form>
        </div>
    );
}