"use client";

import Link from "next/link";
import React from "react";
import { MemberContext, useLoginMember } from "@/stores/member";
import client from '@/lib/axios';

interface ClientLayoutProps {
    children: React.ReactNode;
}

export default function ClientLayout({ children }: Readonly<ClientLayoutProps>) {
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

    const logout = async () => {
        try {
            await client.post('/members/logout');
            removeLoginMember();
        } catch (error) {
            console.error('Logout failed:', error);
        }
    };

    if (isLoginMemberPending) {
        return <div>Loading...</div>;
    }

    return (
        <>
            <header className="flex gap-2">
                <Link href="/">홈</Link>
                <Link href="/p/list">글 목록</Link>
                {isLogin ? (
                    <>
                        <Link href={`/member/me`}>{loginMember?.nickname}님 정보</Link>
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
            <footer>- NextSpring -</footer>
        </>
    );
}