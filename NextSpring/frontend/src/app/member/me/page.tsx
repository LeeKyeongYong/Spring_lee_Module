import ClientPage from "./ClientPage";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import { parseAccessToken } from "@/lib/auth/token";
import { ResponseCookie } from "next/dist/compiled/@edge-runtime/cookies";
import { ReadonlyRequestCookies } from "next/dist/server/web/spec-extension/adapters/request-cookies";

export default async function Page() {
    const cookieStore = await cookies();
    const accessToken = cookieStore.get("accessToken")?.value;

    const { accessTokenPayload } = parseAccessToken(accessToken);


    const { me } = parseAccessToken(accessToken);
    return <ClientPage me={me} />;
}