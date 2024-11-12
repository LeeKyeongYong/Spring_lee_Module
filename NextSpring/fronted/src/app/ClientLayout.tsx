"use client";
import React, { useEffect } from "react";
import { MemberContext, useLoginMember } from "../stores/member";
import Link from "next/link";

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
        setLoginMemberPending,
    };

    const fetchLoginMember = async () => {
        if (typeof window === "undefined") return;

        const token = localStorage.getItem('accessToken');
        console.log('Current token:', token); // 디버깅용

        if (!token) {
            setLoginMemberPending(false);
            removeLoginMember();
            return;
        }

        try {
            setLoginMemberPending(true);
            const baseUrl = process.env.NEXT_PUBLIC_CORE_API_BASE_URL || 'http://localhost:9090/api/v1';
            console.log('Requesting URL:', `${baseUrl}/members/me`); // 디버깅용

            const response = await fetch(`${baseUrl}/members/me`, {
                method: "GET",
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
                credentials: "include",
            });

            console.log('Response status:', response.status); // 디버깅용

            if (!response.ok) {
                const errorData = await response.json();
                console.error("API Error:", errorData);

                if (response.status === 403) {
                    removeLoginMember();
                    localStorage.removeItem('accessToken');
                }
                return;
            }

            const data = await response.json();
            console.log('Response data:', data); // 디버깅용

            if (data.message === "로그인이 필요합니다." || !data.data) {
                removeLoginMember();
                localStorage.removeItem('accessToken');
            } else {
                setLoginMember(data.data.item);
            }
        } catch (error) {
            console.error("Error fetching login member:", error);
            removeLoginMember();
            localStorage.removeItem('accessToken');
        } finally {
            setLoginMemberPending(false);
        }
    };

    useEffect(() => {
        fetchLoginMember();
    }, []);

    const handleLogout = async () => {
        try {
            const baseUrl = process.env.NEXT_PUBLIC_CORE_API_BASE_URL || 'http://localhost:9090/api/v1';
            const token = localStorage.getItem('accessToken');

            await fetch(`${baseUrl}/members/logout`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`,
                },
                credentials: "include",
            });
        } catch (error) {
            console.error("Logout error:", error);
        } finally {
            removeLoginMember();
            localStorage.removeItem('accessToken');
        }
    };

    return (
        <>
            <header>
                <div className="flex gap-2">
                    <Link href="/">NextSpring</Link>
                    {!isLogin && <Link href="/member/login">로그인</Link>}
                    {isLogin && (
                        <button onClick={handleLogout}>
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