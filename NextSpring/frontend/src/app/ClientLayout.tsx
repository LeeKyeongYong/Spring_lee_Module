"use client";

import { components } from "@/lib/backend/apiV1/schema";
import client from "@/lib/backend/client";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { ThemeProvider as NextThemesProvider } from "next-themes";
import * as React from "react";
import { Moon, Sun,MessageCircle  } from "lucide-react";
import { useTheme } from "next-themes";

import { Button } from "@/components/ui/button";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

function ModeToggle() {
    const { setTheme } = useTheme();

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button variant="outline" size="icon">
                    <Sun className="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
                    <Moon className="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
                    <span className="sr-only">테마</span>
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
                <DropdownMenuItem onClick={() => setTheme("light")}>
                    Light
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => setTheme("dark")}>
                    Dark
                </DropdownMenuItem>
                <DropdownMenuItem onClick={() => setTheme("system")}>
                    System
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    );
}

interface ClientLayoutProps {
    children: React.ReactNode;
    me: components["schemas"]["MemberDto"];
    isLogin: boolean;
    isAdmin: boolean;
}

export default function ClientLayout({
                                         children,
                                         me,
                                         isLogin,
                                         isAdmin,
                                     }: ClientLayoutProps): React.ReactElement {
    const pathname = usePathname();
    const isAdmPage = pathname.startsWith("/adm");
    const isUsrPage = !isAdmPage;
    const socialLoginForKakaoUrl = `http://localhost:8080/oauth2/authorization/kakao`;
    const redirectUrlAfterSocialLogin = "http://localhost:3000";

    const logout = async () => {
        const response = await client.DELETE("/api/v1/members/logout");

        if (response.error) {
            alert(response.error.msg);
            return;
        }

        window.location.replace("/");
    };

    const NavigationLinks = () => {
        if (isUsrPage) {
            return (
                <>
                    <Link href="/">
                        <Button variant="link">홈</Button>
                    </Link>
                    <Link href="/about">
                        <Button variant="link">소개</Button>
                    </Link>
                    <Link href="/post/list">
                        <Button variant="link">공개글</Button>
                    </Link>
                    {isLogin && (
                        <Link href="/post/mine">
                            <Button variant="link">내글</Button>
                        </Link>
                    )}
                    {isLogin && (
                        <Link href="/post/write">
                            <Button variant="link">글 쓰기</Button>
                        </Link>
                    )}
                    {!isLogin && (
                        <Link href="/member/join">
                            <Button variant="link">회원가입</Button>
                        </Link>
                    )}
                    {!isLogin && (
                        <Link href="/member/login">
                            <Button variant="link">로그인</Button>
                        </Link>
                    )}
                    {isLogin && (
                        <Button variant="link" onClick={logout}>
                            로그아웃
                        </Button>
                    )}
                    {isLogin && (
                        <Link href="/member/me">
                            <Button variant="link">{me.nickname}님 정보</Button>
                        </Link>
                    )}
                    {isAdmin && (
                        <Link href="/adm">
                            <Button variant="link">관리자</Button>
                        </Link>
                    )}
                </>
            );
        }

        return (
            <>
                <Link href="/adm">
                    <Button variant="link">관리자 홈</Button>
                </Link>
                <Link href="/">
                    <Button variant="link">홈</Button>
                </Link>
                <Button variant="link" onClick={logout}>
                    로그아웃
                </Button>
                <Link href="/adm/member/list">
                    <Button variant="link">회원 목록</Button>
                </Link>
            </>
        );
    };

    return (
        <NextThemesProvider
            attribute="class"
            defaultTheme="system"
            enableSystem
            disableTransitionOnChange
        >
            <header className="border-[2px] border-[red] p-5">
                <div className="flex gap-4">
                    <NavigationLinks />
                    <div className="flex-grow"></div>
                    <ModeToggle />
                </div>
            </header>

            <main className="flex-grow border-[2px] border-[blue] p-5">
                {children}
                <div className="flex-1 flex justify-center items-center">
                    <Button variant="outline" asChild>
                        <a
                            href={`${socialLoginForKakaoUrl}?redirectUrl=${redirectUrlAfterSocialLogin}`}
                        >
                            <MessageCircle />
                            <span className="font-bold">카카오 로그인</span>
                        </a>
                    </Button>
                </div>
            </main>

            <footer className="border-[2px] border-[pink] p-5">
                {isUsrPage && <div>Copyright 2025.</div>}
                {isAdmPage && <div>관리자 페이지 입니다.</div>}
            </footer>
        </NextThemesProvider>
    );
}