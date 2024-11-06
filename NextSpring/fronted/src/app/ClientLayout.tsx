"use client";

import Link from "next/link";
import React from "react";
import { MemberContext, useLoginMember } from "../stores/member";

export default function ClientLayout({
                                         children,
                                     }: Readonly<{
    children: React.ReactNode;
}>) {
    const { loginMember, setLoginMember, removeLoginMember, isLogin } =
        useLoginMember();

    const memberContextValue = {
        loginMember,
        setLoginMember,
        removeLoginMember,
        isLogin,
    };

    return (
        <>
            <header>
                <div className="flex gap-2">
                    <Link href="/">GB</Link>
                    {!isLogin && <Link href="/member/login">로그인</Link>}
                    {isLogin && <button onClick={removeLoginMember}>로그아웃</button>}
                    {isLogin && <span>{loginMember.name}</span>}
                    <Link href="/p/list">글 목록</Link>
                </div>
            </header>
            <MemberContext.Provider value={memberContextValue}>
                <main>{children}</main>
            </MemberContext.Provider>
        </>
    );
}