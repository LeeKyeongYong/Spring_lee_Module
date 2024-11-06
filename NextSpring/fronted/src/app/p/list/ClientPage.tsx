"use client";
import { Post } from "@/app/types/Post";
import Link from "next/link";
import { useEffect, useState } from "react";
import { Page } from "@/app/types/Page";
import { useRouter, useSearchParams } from "next/navigation";

export default function ClientPage() {

    const router = useRouter();

    const [postPage, setPostPage] = useState<Page<Post> | null>(null);
    const searchParams = useSearchParams();
    const currentPageNumber = parseInt(searchParams.get("page") ?? "1");

    const kwType = searchParams.get("kwType") ?? "ALL";
    const kw = searchParams.get("kw") ?? "";

    const pageMenuArmSize = 5;

    useEffect(() => {
        const searchUrl = `http://localhost:8080/api/v1/posts?page=${currentPageNumber}&kwType=${kwType}&kw=${kw}`;
        fetch(searchUrl)
            .then((res) => res.json())
            .then((data) => setPostPage(data));
    }, [currentPageNumber, kwType, kw]);

    const handleSearchFormSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const kwType = formData.get("kwType");
        const kw = formData.get("kw");

        router.push(`/p/list?page=1&kwType=${kwType}&kw=${kw}`);
    };

    return (
        <>

            <form onSubmit={handleSearchFormSubmit}>
                <select name="kwType" defaultValue={kwType}>
                    <option value="ALL">전체</option>
                    <option value="TITLE">제목</option>
                    <option value="BODY">내용</option>
                    <option value="NAME">작성자</option>
                </select>
                <input type="text" name="kw" defaultValue={kw} />
                <button type="submit">검색</button>
            </form>

            <div className="grid">
                {postPage?.content.map((post) => (
                    <Link href={`/p/${post.id}`} key={post.id}>
                        {post.author.nickname} - {post.title}
                    </Link>
                ))}
            </div>

            <Link href="/p/write">글쓰기</Link>

            <div>
                {postPage && (
                    <div className="flex gap-2">
                        {currentPageNumber - pageMenuArmSize > 1 && (
                            <>
                                <Link href={`/p/list?page=1&kwType=${kwType}&kw=${kw}`}>1</Link>
                                <div>...</div>
                            </>
                        )}
                        {Array.from({ length: 100 }, (_, i) => i + 1)
                            .filter((pageNumber) => {
                                const start = currentPageNumber - pageMenuArmSize;
                                const end = Math.min(
                                    currentPageNumber + pageMenuArmSize,
                                    postPage.totalPages
                                );
                                return pageNumber >= start && pageNumber <= end;
                            })
                            .map((pageNumber) => (
                                <Link
                                    href={`/p/list?page=${pageNumber}&kwType=${kwType}&kw=${kw}`}
                                    key={pageNumber}
                                    className={currentPageNumber === pageNumber ? "text-red-500" : ""}
                                >
                                    {pageNumber}
                                </Link>
                            ))}
                        {currentPageNumber + pageMenuArmSize < postPage.totalPages && (
                            <>
                                <div>...</div>
                                <Link
                                    href={`/p/list?page=${postPage.totalPages}&kwType=${kwType}&kw=${kw}`}
                                >
                                    {postPage.totalPages}
                                </Link>
                            </>
                        )}
                    </div>
                )}
            </div>
        </>
    );
}
