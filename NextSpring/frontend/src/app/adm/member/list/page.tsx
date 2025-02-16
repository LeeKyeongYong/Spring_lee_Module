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
    } = searchParams; // await 제거

    try {
        const response = await client.GET("/api/v1/adm/members", {
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

        // 에러 응답 처리
        if (response.error) {
            return <div>Error: {response.error.msg}</div>;
        }

        // response.data를 직접 사용
        const itemPage = response.data;

        return (
            <ClientPage
                searchKeyword={searchKeyword}
                searchKeywordType={searchKeywordType}
                page={page}
                pageSize={pageSize}
                itemPage={itemPage}
            />
        );
    } catch (error) {
        console.error('데이터 로딩 중 오류 발생:', error);
        return <div>데이터를 불러오는 중 오류가 발생했습니다.</div>;
    }
}