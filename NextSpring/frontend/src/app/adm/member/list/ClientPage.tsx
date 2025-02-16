"use client";

import type { components } from "@/lib/backend/apiV1/schema";
import Link from "next/link";
import { useRouter } from "next/navigation";

interface PageDtoMemberWithUsernameDto {
    currentPageNumber: number;
    pageSize: number;
    totalPages: number;
    totalItems: number;
    items: components["schemas"]["MemberWithUsernameDto"][];
}

export default function ClientPage({
                                       searchKeyword,
                                       searchKeywordType,
                                       page,
                                       pageSize,
                                       itemPage,
                                   }: {
    searchKeyword: string;
    searchKeywordType: string;
    page: number;
    pageSize: number;
    itemPage: PageDtoMemberWithUsernameDto;
}) {
    const router = useRouter();

    return (
        <div>
            <form
                onSubmit={(e) => {
                    e.preventDefault();

                    const formData = new FormData(e.target as HTMLFormElement);
                    const searchKeyword = formData.get("searchKeyword") as string;
                    const searchKeywordType = formData.get("searchKeywordType") as string;
                    const page = formData.get("page") as string;
                    const pageSize = formData.get("pageSize") as string;

                    router.push(
                        `?page=${page}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`
                    );
                }}
            >
                <input type="hidden" name="page" value="1" />
                <select name="pageSize" defaultValue={pageSize}>
                    <option disabled>페이지당 행 수</option>
                    <option value="10">10</option>
                    <option value="30">30</option>
                    <option value="50">50</option>
                </select>
                <select name="searchKeywordType" defaultValue={searchKeywordType}>
                    <option disabled>검색어 타입</option>
                    <option value="username">아이디</option>
                    <option value="nickname">별명</option>
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
                <div>현재 페이지: {itemPage.currentPageNumber}</div>
                <div>페이지당 항목 수: {itemPage.pageSize}</div>
                <div>전체 페이지 수: {itemPage.totalPages}</div>
                <div>전체 항목 수: {itemPage.totalItems}</div>
            </div>

            <hr />

            <div className="flex my-2 gap-2">
                {Array.from({ length: itemPage.totalPages }, (_, i) => i + 1).map(
                    (pageNum) => (
                        <Link
                            key={pageNum}
                            className={`px-2 py-1 border rounded ${
                                pageNum === itemPage.currentPageNumber ? "text-red-500" : ""
                            }`}
                            href={`?page=${pageNum}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                        >
                            {pageNum}
                        </Link>
                    )
                )}
            </div>

            <hr />

            <ul>
                {itemPage.items.map((item) => (
                    <li key={item.id} className="border-[2px] border-[red] my-3">
                        <Link className="block" href={`${item.id}`}>
                            <div>id: {item.id}</div>
                            <div>가입일: {item.createDate}</div>
                            <div>아이디: {item.username}</div>
                            <div>별명: {item.nickname}</div>
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
}