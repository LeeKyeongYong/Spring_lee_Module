import type { Metadata } from "next";
import { cookies } from "next/headers";
import { parseAccessToken } from "@/lib/auth/tokens";
import localFont from "next/font/local";
import { config } from "@fortawesome/fontawesome-svg-core";
import "@fortawesome/fontawesome-svg-core/styles.css";
import "./globals.css";
import ClientLayout from "./ClientLayout";
import React from 'react';

// Font Awesome 설정
config.autoAddCss = false;

// Pretendard 폰트 설정
const pretendard = localFont({
    src: "./../../node_modules/pretendard/dist/web/variable/woff2/PretendardVariable.woff2",
    display: "swap",
    weight: "45 920",
    variable: "--font-pretendard",
});

// 메타데이터 설정
export const metadata: Metadata = {
    title: "NextSpring",
    description: "글로그는 당신을 위한 기술 블로그 입니다.",
};

// RootLayout 컴포넌트
export default async function RootLayout({
                                             children,
                                         }: {
    children: React.ReactNode;
}) {
    // 쿠키에서 액세스 토큰 가져오기
    const cookieStore = await cookies();
    const accessToken = cookieStore.get("accessToken")?.value;

    // 토큰 파싱
    const { me, isLogin, isAdmin } = parseAccessToken(accessToken);

    return (
        <html
            lang="ko"
            className={pretendard.variable}
            suppressHydrationWarning
        >
        <head />
        <body
            className={`${pretendard.className} antialiased flex flex-col min-h-[100dvh]`}
        >
        <ClientLayout
            className={`${pretendard.className} antialiased`}
            me={me}
            isLogin={isLogin}
            isAdmin={isAdmin}
        >
            {children}
        </ClientLayout>
        </body>
        </html>
    );
}