"use client";

import { MemberContext } from "@/stores/member";
import { use } from "react";

export default function ClientPage() {
    const { isLogin, loginMember } = use(MemberContext);

    if (!isLogin) return <div>로그인 상태가 아닙니다.</div>;

    return (
        <div>
            <h1>me</h1>
            <p>{loginMember.name}</p>
        </div>
    );
}