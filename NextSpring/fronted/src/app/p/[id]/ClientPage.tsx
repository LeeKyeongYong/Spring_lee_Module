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