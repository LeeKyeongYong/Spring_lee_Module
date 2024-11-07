"use client";

import Link from "next/link";
import React, { useEffect } from "react";
import { MemberContext, useLoginMember } from "../stores/member";

export default function ClientLayout({
                                         children,
                                     }: Readonly<{
    children: React.ReactNode;
}>) {
    const {
        loginMember,
        setLoginMember,
        removeLoginMember,
        isLogin,
        isLoginMemberPending,
        setLoginMemberPending,
    } = useLoginMember();

    const memberContextValue = {
        loginMember,
        setLoginMember,
        removeLoginMember,
        isLogin,
        isLoginMemberPending,
    };

    const fetchLoginMember = async () => {
        const response = await fetch(
            `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/me`,
            {
                credentials: "include",
                method: "GET",
            }
        );

        if (!response.ok) setLoginMemberPending(false);
        if (!response.ok) return;

        const data = await response.json();

        setLoginMember(data.data.item);
        setLoginMemberPending(false);
    };

    useEffect(() => {
        fetchLoginMember();
    }, []);

    return (
        <>
            <header>
                <div className="flex gap-2">
                    <Link href="/">NextSpring</Link>
                    {!isLogin && <Link href="/member/login">로그인</Link>}
                    {isLogin && (
                        <button
                            onClick={async () => {
                                await fetch(
                                    `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/logout`,
                                    {
                                        credentials: "include",
                                        method: "POST",
                                        headers: {
                                            "Content-Type": "application/json",
                                        },
                                    }
                                );

                                removeLoginMember();
                            }}
                        >
                            로그아웃
                        </button>
                    )}
                    {isLogin && <Link href="/member/me">{loginMember.name}</Link>}
                    <Link href="/p/list">글 목록</Link>
                </div>
            </header>
            <MemberContext.Provider value={memberContextValue}>
                <main>{children}</main>
            </MemberContext.Provider>
        </>
    );
}