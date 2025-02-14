"use client";

import Link from "next/link";
import client from "@/lib/backend/client";
import { useRouter } from "next/navigation";

export default function ClientLayout({
                                         children,
                                     }: Readonly<{
    children: React.ReactNode;
}>) {

    const router = useRouter();

    const logout = async () => {
        const response = await client.DELETE("/api/v1/members/logout");

        if (response.error) {
            alert(response.error.msg);
            return;
        }

        router.replace("/");
    };


    return (
        <>
            <header className="border-[2px] border-[red] p-5">
                <div className="flex gap-2">
                    <Link href="/">홈</Link>
                    <Link href="/about">소개</Link>
                    <Link href="/post/list">글</Link>
                    <Link href="/member/login">로그인</Link>
                    <button onClick={logout}>로그아웃</button>
                    <Link href="/member/me">내 정보</Link>
                </div>
            </header>

            <main className="flex-grow border-[2px] border-[blue] p-5">
                {children}
            </main>

            <footer className="border-[2px] border-[pink] p-5">
                Copyright 2025.
            </footer>
        </>
    );
}