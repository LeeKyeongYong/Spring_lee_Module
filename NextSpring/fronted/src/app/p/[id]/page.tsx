import client from "@/lib/openapi_fetch";
import ClientPage from "./ClientPage";
import type { Metadata } from "next";
import { cookies } from "next/headers";

export const metadata: Metadata = {
    title: "",
    description: "",
};

export default async function Page({ params }: { params: { id: string } }) {
    const { id } = await params;

    const { data, error } = await client.GET("/api/v1/posts/{id}", {
        params: {
            path: {
                id: parseInt(id),
            },
        },
        headers: {
            cookie: (await cookies()).toString(),
        },
    });

    if (error) {
        return <div>Error: {error.msg}</div>;
    }

    metadata.title = data.title;
    metadata.description = data.body;

    return <ClientPage id={parseInt(id)} post={data} />;
}
