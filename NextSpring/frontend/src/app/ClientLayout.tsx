"use client";

import Link from "next/link";

export default function ClientLayout({
                                         children,
                                     }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <>
            <header className="border-[2px] border-[red] p-5">
                <div className="flex gap-2">
                    <Link href="/">홈</Link>
                    <Link href="/about">소개</Link>
                    <Link href="/post/list">글</Link>
                    <Link href="/member/login">로그인</Link>
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