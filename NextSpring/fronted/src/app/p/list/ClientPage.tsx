"use client";

import { useEffect, useState } from "react";
import client from "@/lib/openapi_fetch";
import { components } from "@/lib/backend/apiV1/schema";
import Link from "next/link";
import { useSearchParams } from "next/navigation";

export default function ClientPage() {
    //const apiUrl = `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}`;
    const searchParams = useSearchParams();
    const [posts, setPosts] = useState<components["schemas"]["PostDto"][]>([]);
    const [pageable, setPageable] =
        useState<components["schemas"]["PageableDto"]>();

    const currentPage = parseInt(searchParams.get("page") ?? "1");
    const kwType = searchParams.get("kwType") ?? "ALL";
    const kw = searchParams.get("kw") ?? "";

    useEffect(() => {
        client
            .GET("/posts", {
                params: {
                    query: {
                        page: currentPage,
                        kwType: kwType as "ALL" | "TITLE" | "BODY" | "NAME",
                        kw,
                    },
                },
            })
            .then(({ data }) => {
                if (data) {
                    setPosts(data.content);
                    data.pageable && setPageable(data.pageable);
                }
            });
    }, [kwType, kw, currentPage]);

    type PaginationProps = {
        currentPage: number;
        totalPages: number;
        armSize: number;
        kwType: string;
        kw: string;
    };

    function Pagination({
                            currentPage,
                            totalPages,
                            armSize,
                            kwType,
                            kw,
                        }: PaginationProps) {
        const createPageLink = (pageNum: number, label?: string) => (
            <Link
                key={pageNum}
                href={`/p/list?page=${pageNum}&kwType=${kwType}&kw=${kw}`}
                className={`px-3 py-1 border rounded ${
                    currentPage === pageNum
                        ? "bg-blue-500 text-white"
                        : "hover:bg-gray-50"
                }`}
            >
                {label ?? pageNum}
            </Link>
        );

        const getPageNumbers = () => {
            const start = Math.max(1, currentPage - armSize);
            const end = Math.min(currentPage + armSize, totalPages);
            return Array.from({ length: end - start + 1 }, (_, i) => start + i);
        };

        return (
            <div className="flex gap-2 items-center justify-center mt-4">
                {currentPage - armSize > 1 && (
                    <>
                        {createPageLink(1)}
                        <span>...</span>
                    </>
                )}

                {getPageNumbers().map((pageNumber) => createPageLink(pageNumber))}

                {currentPage + armSize < totalPages && (
                    <>
                        <span>...</span>
                        {createPageLink(totalPages)}
                    </>
                )}
            </div>
        );
    }

    const PAGE_MENU_ARM_SIZE = 5;

    return (
        <>
            <form>
                <input type="text" name="kw" placeholder="검색어" defaultValue={kw} />
                <select name="kwType" defaultValue={kwType}>
                    <option value="ALL">모두</option>
                    <option value="TITLE">제목</option>
                    <option value="BODY">내용</option>
                    <option value="NAME">작성자</option>
                </select>
                <button type="submit">검색</button>
            </form>

            <ul>
                {posts.map((post) => (
                    <li key={post.id}>
                        <Link href={`/p/${post.id}`}>{post.title}</Link>
                    </li>
                ))}
            </ul>

            {pageable && (
                <Pagination
                    currentPage={currentPage}
                    totalPages={pageable.totalPages}
                    armSize={PAGE_MENU_ARM_SIZE}
                    kwType={kwType}
                    kw={kw}
                />
            )}
        </>
    );
}
