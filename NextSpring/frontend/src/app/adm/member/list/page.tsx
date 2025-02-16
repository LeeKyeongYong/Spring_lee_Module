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
    } = searchParams;

    try {
        const response = await client.GET("/api/v1/adm/members", {
            params: {
                query: {
                    searchKeywordType,
                    searchKeyword,
                    page,
                    pageSize,
                },
            },
            headers: {
                cookie: cookies().toString(),
            },
        });

        if (!response || !response.data) {
            throw new Error('API 응답이 올바르지 않습니다.');
        }

        // API 응답 구조에 맞게 데이터 추출
        const itemPage = {
            currentPageNumber: page,
            pageSize: pageSize,
            totalPages: response.data.totalPages,
            totalItems: response.data.totalElements,
            items: response.data.content.map((item: any) => ({
                id: item.id,
                createDate: item.createDate,
                username: item.username,
                nickname: item.nickname,
            })),
        };

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