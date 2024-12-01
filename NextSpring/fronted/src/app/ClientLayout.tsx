"use client";

import Link from "next/link";

import client from "@/lib/openapi_fetch";
import { MemberContext, useLoginMember } from "@/stores/member";
import { useEffect } from "react";

export default function ClientLayout({
                                         children,
                                     }: Readonly<{
    children: React.ReactNode;
}>) {
    const {
        setLoginMember,
        isLogin,
        loginMember,
        removeLoginMember,
        isLoginMemberPending,
    } = useLoginMember();

    const memberContextValue = {
        loginMember,
        setLoginMember,
        removeLoginMember,
        isLogin,
        isLoginMemberPending,
    };

    useEffect(() => {
        client.GET("/api/v1/members/me").then(({ data }) => {
            if (data) {
                setLoginMember(data.data);
            }
        });
    }, []);

    const logout = () => {
        client.POST("/api/v1/members/logout").then(({ error }) => {
            if (error) {
                alert(error.msg);
            } else {
                removeLoginMember();
            }
        });
    };

    return (
        <>
            <header className="flex gap-2">
                <Link href="/">홈</Link>
                <Link href="/p/list">글 목록</Link>
                {isLogin ? (
                    <>
                        <Link href={`/member/me`}>{loginMember.name}님 정보</Link>
                        <button onClick={logout}>로그아웃</button>
                    </>
                ) : (
                    <>
                        <Link href="/member/login">로그인</Link>
                        <Link href="/member/join">회원가입</Link>
                    </>
                )}
            </header>
            <MemberContext.Provider value={memberContextValue}>
                <main className="flex-1">{children}</main>
            </MemberContext.Provider>
            <footer>- GB4 -</footer>
        </>
    );
}
