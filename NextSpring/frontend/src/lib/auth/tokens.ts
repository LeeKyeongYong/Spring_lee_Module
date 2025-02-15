import { components } from "../backend/apiV1/schema";

export function parseAccessToken(accessToken: string | undefined) {
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

    const isAdmin =
        isLogin && accessTokenPayload.authorities.includes("ROLE_ADMIN");


    const me: components["schemas"]["MemberDto"] | null = isLogin
        ? {
            id: accessTokenPayload.id,
            createDate: "",
            modifyDate: "",
            nickname: accessTokenPayload.nickname,
        }
        : {
            id: 0,
            createDate: "",
            modifyDate: "",
            nickname: "",
        };

    return { isLogin, isAdmin, isAccessTokenExpired, accessTokenPayload, me };
}