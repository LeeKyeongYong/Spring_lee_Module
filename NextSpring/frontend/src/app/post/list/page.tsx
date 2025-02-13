import type { paths } from "@/lib/backend/apiV1/schema";
import createClient from "openapi-fetch";
import ClientPage from "./ClientPage";

const client = createClient<paths>({
    baseUrl: "http://localhost:8080",
});

export default async function Page({
                                       searchParams,
                                   }: {
    searchParams: {
        searchKeywordType?: "title" | "content";
        searchKeyword?: string;
        pageSize?: number;
        page?: number;
    };
}) {
    const {
        searchKeyword = "",
        searchKeywordType = "title",
        pageSize = 10,
        page = 1,
    } = await searchParams;


    const response = await client.GET("/api/v1/posts", {
        params: {
            query: {
                searchKeyword,
                searchKeywordType,
                pageSize,
                page,
            },
        },
    });

    const responseBody = response.data!!;


    return (
       <>
           <ClientPage
               searchKeyword={searchKeyword}
               searchKeywordType={searchKeywordType}
               page={page}
               pageSize={pageSize}
               responseBody={responseBody}
           />
       </>
    );
}
