"use client";
import { components } from "@/lib/backend/apiV1/schema";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function ClientPage({
                                       post,
                                   }: {
    post: components["schemas"]["PostWithContentDto"];
}) {
    const router = useRouter();

    return (
        <div>
            <button onClick={() => router.back()}>뒤로가기</button>
            <hr />
            {post.id}번 게시물 상세페이지
            <hr />
            작성날짜 : {post.createDate}
            <hr />
            수정날짜 : {post.modifyDate}
            <hr />
            <h1>{post.title}</h1>
            <hr />
            <p>{post.content}</p>
            <div>
                <Link href={`/post/${post.id}/edit`}>수정</Link>
            </div>
        </div>
    );
}