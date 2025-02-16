import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import ClientPage from "./ClientPage";

export default async function Page({ params }: { params: { id: number } }) {
    const { id } = await params;

    const response = await client.GET("/api/v1/adm/members/{id}", {
        params: {
            path: {
                id,
            },
        },
        headers: {
            cookie: (await cookies()).toString(),
        },
    });

    if (response.error) {
        return <>{response.error.msg}</>;
    }

    const member = response.data!!;

    return <ClientPage member={member} />;
}