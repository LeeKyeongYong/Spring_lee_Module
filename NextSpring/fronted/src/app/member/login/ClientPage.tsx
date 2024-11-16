"use client";

import client from "@/lib/openapi_fetch";
import { MemberContext } from "@/stores/member";
import { useRouter } from "next/navigation";
import { use } from "react";

export default function ClientPage() {
    const router = useRouter();
    const { setLoginMember, isLogin } = use(MemberContext);

    if (isLogin) {
        return <div>이미 로그인 상태입니다.</div>;
    }

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const formData = new FormData(e.target as HTMLFormElement);
        const username = formData.get("username") as string;
        const password = formData.get("password") as string;

        const { data, error } = await client.POST("/members/login", {
            body: {
                username,
                password,
            },
        });

        if (error) {
            alert(error.msg);
        } else {
            setLoginMember(data.data.item);
            router.push("/");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <input name="username" type="text" required placeholder="아이디" />
                <input
                    name="password"
                    type="password"
                    required
                    placeholder="비밀번호"
                />
                <button type="submit">로그인</button>
            </div>
        </form>
    );
}
