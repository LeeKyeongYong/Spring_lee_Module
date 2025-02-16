"use client";

import { components } from "@/lib/backend/apiV1/schema";
import client from "@/lib/backend/client";
import { useRouter } from "next/navigation";

export default function ClientPage({
                                       me,
                                   }: {
    me: components["schemas"]["MemberDto"];
}) {
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const form = e.target as HTMLFormElement;
        const nickname = form.nickname.value;

        const response = await client.PUT("/api/v1/members/me", {
            body: {
                nickname,
            },
        });

        if (response.error) {
            alert(response.error.msg);
            return;
        }

        window.location.replace("/member/me");
    };

    return (
        <div>
            <button onClick={() => router.back()}>뒤로가기</button>
            <form onSubmit={handleSubmit}>
                <div>ID : {me.id}</div>
                <div>기존별명 : {me.nickname}</div>
                <div>
                    새 별명
                    <input
                        className="border p-2"
                        type="text"
                        name="nickname"
                        defaultValue={me.nickname}
                    />
                </div>
                <div>
                    <button type="submit">변경</button>
                </div>
            </form>
        </div>
    );
}