import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";
import ClientLayout from "./ClientLayout";

const pretendard = localFont({
    src: "./fonts/PretendardVariable.woff2",
    display: "swap",
    weight: "45 920",
    variable: "--font-pretendard",
});

export const metadata: Metadata = {
    title: "NextSpring",
    description: "개발자를 위한 블로그 솔루션",
};

export default function RootLayout({
                                       children,
                                   }: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="ko" className={`${pretendard.variable}`}>
        <body className={`${pretendard.className} flex flex-col min-h-screen`}>
        <ClientLayout>{children}</ClientLayout>
        </body>
        </html>
    );
}
