"use client";

import { MemberContext } from "@/stores/member";
import { useAuth } from "@/hooks/useAuth";
import { useRouter } from "next/navigation";
import { useEffect } from "react";  // 추가된 부분

export default function ClientPage() {
    const { getAccessToken, isLoggedIn } = useAuth();
    const router = useRouter();

    // 아직 초기화가 안 됐으면 아무것도 렌더링하지 않음
    useEffect(() => {
        if (!isLoggedIn()) {
            alert("로그인이 필요합니다.");
            router.push("/login");
        }
    }, [isLoggedIn, router]);

    if (!isLoggedIn()) {
        return null;
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const title = formData.get("title") as string;
        const body = formData.get("body") as string;
        const postData = { title, body, isPublished: true, isListed: true };

        try {
            const accessToken = await getAccessToken();
            const response = await fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/api/v1/posts`, {
                method: "POST",
                body: JSON.stringify(postData),
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${accessToken}`,
                },
            });

            if (!response.ok) {
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
                <input type="text" name="title" placeholder="제목" />
                <textarea name="body" placeholder="내용" />
                <button type="submit">등록</button>
            </form>
        </div>
    );
}