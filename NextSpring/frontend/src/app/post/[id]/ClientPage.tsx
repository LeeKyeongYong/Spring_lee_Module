"use client";

import { useRouter } from "next/navigation";

export default function ClientPage({ id }: { id: string }) {
    const router = useRouter();

    return (
        <div>
            <button onClick={() => router.back()}>뒤로가기</button>
            <hr />
            {id}번 게시물 상세페이지
        </div>
    );
}