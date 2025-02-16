import client from "@/lib/backend/client";
import { cookies } from "next/headers";
import ClientPage from "./ClientPage";

export default async function Page({
                                       searchParams,
                                   }: {
    searchParams: {
        searchKeywordType?: "username" | "nickname";
        searchKeyword?: string;
        pageSize?: string | number;
        page?: string | number;
    };
}) {
    // searchParams를 await로 처리
    const params = await searchParams;

    // 값 변환 및 기본값 설정
    const searchKeyword = String(params.searchKeyword || "");
    const searchKeywordType = (params.searchKeywordType || "username") as "username" | "nickname";
    const pageSize = Number(params.pageSize || 10);
    const page = Number(params.page || 1);

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

        if (response.error) {
            return <div>Error: {response.error.msg}</div>;
        }

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