"use client";

import { components } from "@/lib/backend/apiV1/schema";

export default function ClientPage({
                                       id,
                                       post,
                                   }: {
    id: number;
    post: components["schemas"]["PostDto"];
}) {
    return (
        <div>
            <h1>{post.title}</h1>
            <p>
                {post.id} : {post.body}
            </p>
        </div>
    );
}


/*
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

 */