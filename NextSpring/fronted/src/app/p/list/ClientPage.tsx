"use client";
import { Post } from "@/app/types/Post";
import Link from "next/link";
import { useEffect, useState } from "react";
import { Page } from "@/app/types/Page";
import { useSearchParams } from "next/navigation";

export default function ClientPage() {
    const [postPage, setPostPage] = useState<Page<Post> | null>(null);
    const searchParams = useSearchParams();
    const currentPageNumber = parseInt(searchParams.get("page") ?? "1");

    const pageMenuArmSize = 5;

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/posts?page=${currentPageNumber}`)
            .then((res) => res.json())
            .then((data) => setPostPage(data));
    }, [currentPageNumber]);

    return (
        <>
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
                                <Link href={`/p/list?page=1`}>1</Link>
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
                                    href={`/p/list?page=${pageNumber}`}
                                    key={pageNumber}
                                    className={currentPageNumber === pageNumber ? "text-red-500" : ""}
                                >
                                    {pageNumber}
                                </Link>
                            ))}
                        {currentPageNumber + pageMenuArmSize < postPage.totalPages && (
                            <>
                                <div>...</div>
                                <Link href={`/p/list?page=${postPage.totalPages}`}>
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
