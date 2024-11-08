"use client";

import React, { useEffect, useState } from 'react';
import { useRouter, useSearchParams } from 'next/navigation';
import Link from 'next/link';
import { Post } from '@/types/Post';

type SearchFormProps = {
    initialKwType: string;
    initialKw: string;
};

export function SearchForm({ initialKwType, initialKw }: SearchFormProps) {
    const router = useRouter();

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const formData = new FormData(e.currentTarget);
        const kwType = formData.get('kwType') as string;
        const kw = formData.get('kw') as string;

        router.push(`/p/list?page=1&kwType=${kwType}&kw=${kw}`);
    };

    return (
        <form onSubmit={handleSubmit} className="mb-4">
            <select
                name="kwType"
                defaultValue={initialKwType}
                className="mr-2 p-2 border rounded"
            >
                <option value="ALL">전체</option>
                <option value="TITLE">제목</option>
                <option value="BODY">내용</option>
                <option value="NAME">작성자</option>
            </select>
            <input
                type="text"
                name="kw"
                defaultValue={initialKw}
                className="mr-2 p-2 border rounded"
            />
            <button
                type="submit"
                className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600"
            >
                검색
            </button>
        </form>
    );
}

type PostListProps = {
    posts: Post[];
};

export function PostList({ posts }: PostListProps) {
    if (!posts || posts.length === 0) {
        return <div>No posts to display.</div>;
    }

    return (
        <div className="grid gap-4">
            {posts.map((post) => (
                <Link
                    href={`/p/${post.id}`}
                    key={post.id}
                    className="p-4 border rounded hover:bg-gray-50"
                >
                    <div className="flex justify-between">
                        <span className="font-medium">{post.title}</span>
                        <span className="text-gray-600">{post.authorName}</span>
                    </div>
                </Link>
            ))}
        </div>
    );
}

type PaginationProps = {
    currentPage: number;
    totalPages: number;
    armSize: number;
    kwType: string;
    kw: string;
};

export function Pagination({
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
                currentPage === pageNum ? 'bg-blue-500 text-white' : 'hover:bg-gray-50'
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

export default function PostListPage() {
    const [postPage, setPostPage] = useState<{ content: Post[]; totalPages: number } | null>(null);
    const [error, setError] = useState<string | null>(null);
    const router = useRouter();
    const searchParams = useSearchParams();

    const currentPage = parseInt(searchParams.get('page') || '1');
    const kwType = searchParams.get('kwType') || 'ALL';
    const kw = searchParams.get('kw') || '';

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await fetch(
                    `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/posts?page=${currentPage - 1}&kwType=${kwType}&kw=${kw}`
                );

                if (!response.ok) {
                    throw new Error(`HTTP error ${response.status}`);
                }

                const data = await response.json();
                setPostPage(data);
                setError(null);
            } catch (error) {
                console.error('Failed to fetch posts:', error);
                setError('Failed to fetch posts. Please try again later.');
            }
        };

        fetchPosts();
    }, [currentPage, kwType, kw]);

    if (error) {
        return <div>{error}</div>;
    }

    if (!postPage) {
        return <div>Loading...</div>;
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <SearchForm initialKwType={kwType} initialKw={kw} />

            <div className="mb-4 flex justify-end">
                <Link
                    href="/p/write"
                    className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                >
                    글쓰기
                </Link>
            </div>

            <PostList posts={postPage.content} />

            <Pagination
                currentPage={currentPage}
                totalPages={postPage.totalPages}
                armSize={PAGE_MENU_ARM_SIZE}
                kwType={kwType}
                kw={kw}
            />
        </div>
    );
}