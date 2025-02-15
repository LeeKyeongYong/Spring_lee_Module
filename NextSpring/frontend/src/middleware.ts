import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { cookies } from "next/headers";
import client from "./lib/backend/client";
import { ResponseCookie } from "next/dist/compiled/@edge-runtime/cookies";

export async function middleware(req: NextRequest) {

    const cookieStore = await cookies();
    const accessToken = cookieStore.get("accessToken")?.value;


    const { isLogin, isAdmin, isAccessTokenExpired } =
        parseAccessToken(accessToken);

    if (isLogin && isAccessTokenExpired) await refreshTokens(cookieStore);

    if (requiresLogin(req.nextUrl.pathname) && !isLogin)
        return createUnauthorizedResponse("로그인 후 이용해주세요.");

    if (requiresAdmin(req.nextUrl.pathname) && !isAdmin)
        return createForbiddenResponse("관리자로 로그인 후 다시 이용해주세요.");

    return NextResponse.next({
        headers: {
            cookie: cookieStore.toString(),
        },
    });
}

function parseAccessToken(accessToken: string | undefined) {

    let isAccessTokenExpired = true;
    let accessTokenPayload = null;

    if (accessToken) {
        try {
            const tokenParts = accessToken.split(".");
            accessTokenPayload = JSON.parse(
                Buffer.from(tokenParts[1], "base64").toString()
            );

            const expTimestamp = accessTokenPayload.exp * 1000;
            isAccessTokenExpired = Date.now() > expTimestamp;
        } catch (e) {
            console.error("토큰 파싱 중 오류 발생:", e);
        }
    }

    const isLogin =
        typeof accessTokenPayload === "object" && accessTokenPayload !== null;

    return { isLogin, isAccessTokenExpired, accessTokenPayload };
}

async function refreshTokens(cookieStore: ReadonlyRequestCookies) {
    const meResponse = await client.GET("/api/v1/members/me", {
        headers: {
            cookie: cookieStore.toString(),
        },
    });

    const setCookieHeader = meResponse.response.headers.get("Set-Cookie");

    if (setCookieHeader) {
        const cookies = setCookieHeader.split(",");

        for (const cookieStr of cookies) {
            const cookieData = parseCookie(cookieStr);

            if (cookieData) {
                const { name, value, options } = cookieData;
                if (name !== "accessToken" && name !== "apiKey") return null;

                cookieStore.set(name, value, options);

            }
        }
    }
 }

function parseCookie(cookieStr: string) {
    const parts = cookieStr.split(";").map((p) => p.trim());
    const [name, value] = parts[0].split("=");

    const options: Partial<ResponseCookie> = {};
    for (const part of parts.slice(1)) {
        if (part.toLowerCase() === "httponly") options.httpOnly = true;
        else if (part.toLowerCase() === "secure") options.secure = true;
        else {
            const [key, val] = part.split("=");
            const keyLower = key.toLowerCase();
            if (keyLower === "domain") options.domain = val;
            else if (keyLower === "path") options.path = val;
            else if (keyLower === "max-age") options.maxAge = parseInt(val);
            else if (keyLower === "expires")
                options.expires = new Date(val).getTime();
            else if (keyLower === "samesite")
                options.sameSite = val.toLowerCase() as "lax" | "strict" | "none";
        }
    }

    return { name, value, options };
}

function requiresLogin(pathname: string): boolean {
    return (
        pathname.startsWith("/post/write") ||
        pathname.match(/^\/post\/\d+\/edit$/) !== null ||
        pathname.startsWith("/member/me")
    );
}

function requiresAdmin(pathname: string): boolean {
    return pathname.startsWith("/adm");
}

function createUnauthorizedResponse(msg: string): NextResponse {
    return new NextResponse(msg, {
        status: 401,
        "Content-Type": "text/html; charset=utf-8",
        },
    });
}

function createForbiddenResponse(msg: string): NextResponse {
    return new NextResponse(msg, {
        status: 403,
        headers: {
            "Content-Type": "text/html; charset=utf-8",
        },
    });
}

export const config = {
    // 아래 2가지 경우에는 middleware를 실행하지 않도록 세팅
    // api 로 시작하거나 하는 요청 : /api/~~~
    // 정적 파일 요청 : /~~~.jpg, /~~~.png, /~~~.css, /~~~.js
    // PS. 여기서 말하는 api 로 시작하는 요청은 백엔드 API 서버로의 요청이 아니라 Next.js 고유의 API 서버로의 요청이다.
    // PS. 우리는 현재 이 기능을 사용하고 있지 않다.
    matcher: "/((?!.*\\.|api\\/).*)",
};