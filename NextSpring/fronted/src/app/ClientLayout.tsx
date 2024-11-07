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
        setLoginMemberPending,  // 추가
    };

    const fetchLoginMember = async () => {
        const token = localStorage.getItem('accessToken');

        if (!token) {
            console.error("No token found");
            setLoginMemberPending(false);
            return;
        }

        try {
            const response = await fetch(
                `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/me`,
                {
                    credentials: "include",
                    method: "GET",
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json',
                    },
                }
            );

            if (!response.ok) {
                console.error("Failed to fetch login member");
                setLoginMemberPending(false);
                return;
            }

            const data = await response.json();

            if (data.resultCode === "403-1") {
                console.error("User not authenticated");
            } else {
                setLoginMember(data.data.item);
                setLoginMemberPending(false);
            }
        } catch (error) {
            console.error("Error fetching login member:", error);
            setLoginMemberPending(false);
        }
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
                    {isLogin && <Link href="/member/me">나의정보</Link>}

                    <Link href="/p/list">글 목록</Link>
                </div>
            </header>
            <MemberContext.Provider value={memberContextValue}>
                <main>{children}</main>
            </MemberContext.Provider>
        </>
    );
}