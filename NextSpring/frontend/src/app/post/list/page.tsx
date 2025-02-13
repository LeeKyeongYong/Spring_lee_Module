import createClient from "openapi-fetch";
import Link from "next/link";
import type { paths } from "@/lib/backend/apiV1/schema";

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
        <div>
            <form>
                <input type="hidden" name="page" value="1" />
                <select name="pageSize" defaultValue={pageSize}>
                    <option disabled>페이당 행 수</option>
                    <option value="10">10</option>
                    <option value="30">30</option>
                    <option value="50">50</option>
                </select>

                <select name="searchKeywordType" defaultValue={searchKeywordType}>
                    <option disabled>검색어 타입</option>
                    <option value="title">제목</option>
                    <option value="content">내용</option>
                </select>
                <input
                    placeholder="검색어를 입력해주세요."
                    type="text"
                    name="searchKeyword"
                    defaultValue={searchKeyword}
                />
                <button type="submit">검색</button>
            </form>

            <div>
                <div>currentPageNumber: {responseBody.currentPageNumber}</div>

                <div>pageSize: {responseBody.pageSize}</div>

                <div>totalPages: {responseBody.totalPages}</div>

                <div>totalItems: {responseBody.totalItems}</div>
            </div>

            <hr />

            <ul>
                {responseBody.items.map((item) => (
                    <li key={item.id} className="border-[2px] border-[red] my-3">
                        <div>id : {item.id}</div>
                        <div>createDate : {item.createDate}</div>
                        <div>modifyDate : {item.modifyDate}</div>
                        <div>authorId : {item.authorId}</div>
                        <div>authorName : {item.authorName}</div>
                        <div>title : {item.title}</div>
                        <div>published : {`${item.published}`}</div>
                        <div>listed : {`${item.listed}`}</div>
                    </li>
                ))}
            </ul>

            <div className="flex gap-2">
                {Array.from({ length: responseBody.totalPages }, (_, i) => i + 1).map(
                    (pageNum) => (
                        <Link
                            key={pageNum}
                            className="px-2 py-1 border rounded"
                            href={`?page=${pageNum}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                        >
                            {pageNum}
                        </Link>
                    )
                )}
            </div>

        </div>
    );
}
