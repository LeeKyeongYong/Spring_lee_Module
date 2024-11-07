"use client";

import { MemberContext } from "@/stores/member";
import { useRouter } from "next/navigation";
import { use } from "react";

export default function ClientPage() {
    const { isLogin, isLoginMemberPending } = use(MemberContext);

    if (isLoginMemberPending) {
        return <div>로그인 체크 중...</div>;
    }

    const router = useRouter();

    if (!isLogin) {
        alert("로그인이 필요합니다.");
        router.back();
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const formData = new FormData(e.target as HTMLFormElement);
        const title = formData.get("title") as string;
        const body = formData.get("body") as string;

        await fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/posts`, {
            method: "POST",
            body: JSON.stringify({ title, body }),
            headers: {
                "Content-Type": "application/json",
            },
        });

        alert("등록되었습니다.");

        router.back();
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