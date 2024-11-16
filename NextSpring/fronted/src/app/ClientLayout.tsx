"use client";

import Link from "next/link";
import client from "@/lib/openapi_fetch";
import { MemberContext, useLoginMember } from "@/stores/member";
import { useEffect } from "react";

export default function ClientLayout({ children }: Readonly<{ children: React.ReactNode }>) {
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
        // 로그인 상태가 아닐 때는 API 호출 건너뛰기
        if (!localStorage.getItem("accessToken")) {
            console.log("로그인 상태가 아님");
            return;
        }

        console.log('Base URL:', process.env.NEXT_PUBLIC_CORE_API_BASE_URL);

        client.GET('/members/me')
            .then(({ data, error }) => {
                if (error) {
                    console.error('API 에러:', error);
                    return;
                }
                if (data?.data) {
                    setLoginMember(data.data);
                }
            })
            .catch((error) => {
                console.error('API 요청 에러:', error);
                // 401 에러인 경우 토큰 제거
                if (error.response?.status === 401) {
                    localStorage.removeItem("accessToken");
                    removeLoginMember();
                }
            });
    }, [setLoginMember, removeLoginMember]);

    const logout = () => {
        client.POST('/members/logout')
            .then(({ error }) => {
                if (error) {
                    alert(error.msg);
                } else {
                    removeLoginMember();
                }
            })
            .catch((error) => {
                console.error('로그아웃 에러:', error);
            });
    };

    return (
        <>
            <header className="flex gap-2">
                <Link href="/">홈</Link>
                <Link href="/p/list">글 목록</Link>
                {isLogin ? (
                    <>
                        <Link href="/member/me">{loginMember?.name}님 정보</Link>
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