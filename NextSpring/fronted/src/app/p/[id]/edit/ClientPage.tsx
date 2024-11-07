"use client";

import { MemberContext } from "@/stores/member";
import { Post } from "@/types/post";
import { useRouter } from "next/navigation";

import { use, useEffect, useState } from "react";
export default function ClientPage({ id }: { id: string }) {
    const { isLogin, loginMember, isLoginMemberPending } = use(MemberContext);

    if (isLoginMemberPending) {
        return <div>로그인 체크 중...</div>;
    }

    const router = useRouter();
    const [post, setPost] = useState<Post | null>(null);

    if (!isLogin) {
        alert("로그인이 필요합니다.");
        router.back();
        return;
    }

    useEffect(() => {
        fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/posts/${id}`, {
            credentials: "include",
        })
            .then((res) => {
                if (!res.ok) throw new Error("권한이 없습니다.");

                return res;
            })
            .then((res) => res.json())
            .then((data) => {
                if (data.authorId !== loginMember.id)
                    throw new Error("권한이 없습니다.");

                return data;
            })
            .then((data) => setPost(data))
            .catch((err) => {
                alert(err.message);
                router.back();
            });
    }, []);

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const formData = new FormData(e.target as HTMLFormElement);
        const title = formData.get("title") as string;
        const body = formData.get("body") as string;

        await fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/posts/${id}`, {
            credentials: "include",
            method: "PUT",
            body: JSON.stringify({ title, body }),
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((data) => data.json())
            .then((res) => {
                if (!res.ok) throw new Error("권한이 없습니다.");

                return res;
            })
            .catch((err) => {
                alert(err.message);
                router.back();
            });

        alert("수정되었습니다.");

        router.replace(`/p/${id}`);
    };

    return (
        <div>
            {post && (
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="title"
                        placeholder="제목"
                        defaultValue={post.title}
                    />
                    <textarea name="body" placeholder="내용" defaultValue={post.body} />
                    <button type="submit">수정</button>
                </form>
            )}
        </div>
    );
}