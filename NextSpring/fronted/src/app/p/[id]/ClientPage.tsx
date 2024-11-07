"use client";

import { Post } from "@/types/post";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function ClientPage({ id, post }: { id: string; post: Post }) {
    const router = useRouter();

    const deletePost = async () => {
        await fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/posts/${id}`, {
            credentials: "include",
            method: "DELETE",
        });

        alert("삭제되었습니다.");

        router.back();
    };

    return (
        <div className="grid">
            {post && (
                <div>
                    <div>{post.id}</div>
                    <div>{post.title}</div>
                    <div>{post.createDate}</div>
                    <div>{post.modifyDate}</div>
                    <div>{post.body}</div>
                    <button onClick={() => router.back()}>뒤로가기</button>
                    {post.actorCanModify && <Link href={`/p/${post.id}/edit`}>수정</Link>}
                    {post.actorCanDelete && <button onClick={deletePost}>삭제</button>}
                </div>
            )}
        </div>
    );
}