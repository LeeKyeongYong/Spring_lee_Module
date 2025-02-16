import client from "@/lib/backend/client";

import { cookies } from "next/headers";
import ClientPage from "./ClientPage";

export default async function Page({
                                       searchParams,
                                   }: {
    searchParams: {
        searchKeywordType?: "username" | "nickname";
        searchKeyword?: string;
        pageSize?: number;
        page?: number;
    };
}) {
    const {
        searchKeyword = "",
        searchKeywordType = "username",
        pageSize = 10,
        page = 1,
    } = await searchParams;

    const response = await client.GET("/api/v1/members", {
        params: {
            query: {
                searchKeyword,
                searchKeywordType,
                pageSize,
                page,
            },
        },
        headers: {
            cookie: (await cookies()).toString(),
        },
    });

    const itemPage = response.data!!;

    return (
        <>
            <ClientPage
                searchKeyword={searchKeyword}
                searchKeywordType={searchKeywordType}
                page={page}
                pageSize={pageSize}
                itemPage={itemPage}
            />
        </>
    );
}