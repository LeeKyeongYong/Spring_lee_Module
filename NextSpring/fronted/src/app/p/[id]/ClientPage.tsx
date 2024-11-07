"use client";

import { Post } from "@/types/post";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function ClientPage({ id, post }: { id: string; post: Post }) {

    const router = useRouter();

    const deletePost = async ()=>{
        await fetch(`http://localhost:8080/api/v1/posts/${id}`,{
            method:"DELETE",
        });
        alert("삭제 되었습니다.");

        router.back();
    }

    return(
        <div className="grid">
            {post && (
                <div>
                    <div>{post.id}</div>
                    <div>{post.title}</div>
                    <div>{post.createDate}</div>
                    <div>{post.modifyDate}</div>
                    <div>{post.body}</div>
                    <button onClick={() => router.back()}>뒤로가기</button>
                    <button onClick={deletePost}>삭제</button>
                    <Link href={`/p/${post.id}/edit`}>수정</Link>
                </div>
            )}
        </div>
    );
}