import ClientPage from "./ClientPage";
import client from "@/lib/backend/client";
import { cookies } from "next/headers";

export default async function Page() {
    const response = await client.GET("/api/v1/members/me", {
        headers: {
            cookie: (await cookies()).toString(),
        },
    });

    if (response.error) {
        return <>{response.error.msg}</>;
    }

    const me = response.data;

    return <ClientPage me={me} />;
}