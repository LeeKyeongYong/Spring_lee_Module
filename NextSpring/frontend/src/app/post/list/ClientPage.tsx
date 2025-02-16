"use client";

import type { components } from "@/lib/backend/apiV1/schema";
import Link from "next/link";
import { useRouter } from "next/navigation";

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
    itemPage: components["schemas"]["PageDtoPostDto"];
}) {
    const router = useRouter();

    const totalPages = itemPage.pageable.totalPages;
    const currentPage = itemPage.pageable.pageNumber + 1; // 페이지는 1부터 시작
    const pagesToShow = 5; // 페이지 링크에서 보여줄 페이지 수
    const pageLinks = [];

    // 앞뒤로 페이지를 나누어 표시 (예: 3페이지 앞뒤)
    const startPage = Math.max(1, currentPage - Math.floor(pagesToShow / 2));
    const endPage = Math.min(totalPages, currentPage + Math.floor(pagesToShow / 2));

    // 첫 번째 페이지
    if (currentPage > 1) {
        pageLinks.push(
            <Link
                key="first"
                href={`?page=1&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                className="px-2 py-1 border rounded"
            >
                First
            </Link>
        );

        pageLinks.push(
            <Link
                key="prev"
                href={`?page=${currentPage - 1}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                className="px-2 py-1 border rounded"
            >
                Prev
            </Link>
        );
    }

    // 중간 페이지 번호들
    for (let i = startPage; i <= endPage; i++) {
        pageLinks.push(
            <Link
                key={i}
                href={`?page=${i}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                className={`px-2 py-1 border rounded ${i === currentPage ? "text-red-500" : ""}`}
            >
                {i}
            </Link>
        );
    }

    // 마지막 페이지
    if (currentPage < totalPages) {
        pageLinks.push(
            <Link
                key="next"
                href={`?page=${currentPage + 1}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                className="px-2 py-1 border rounded"
            >
                Next
            </Link>
        );

        pageLinks.push(
            <Link
                key="last"
                href={`?page=${totalPages}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
                className="px-2 py-1 border rounded"
            >
                Last
            </Link>
        );
    }

    return (
        <div>
            <h1 className="text-2xl font-bold">공개글 목록</h1>
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
                <div>currentPageNumber: {itemPage.pageable.pageNumber}</div>
                <div>pageSize: {itemPage.pageable.pageSize}</div>
                <div>totalPages: {itemPage.pageable.totalPages}</div>
                <div>totalItems: {itemPage.pageable.totalElements}</div>
            </div>

            <hr />

            <div className="flex my-2 gap-2">{pageLinks}</div>

            <hr />

            <ul>
                {itemPage.content.length === 0 ? (
                    <div>No posts available.</div>
                ) : (
                    itemPage.content.map((item) => (
                        <li key={item.id} className="border-[2px] border-[red] my-3">
                            <Link className="block" href={`/post/${item.id}`}>
                                <div>id : {item.id}</div>
                                <div>createDate : {item.createDate}</div>
                                <div>modifyDate : {item.modifyDate}</div>
                                <div>authorId : {item.authorId}</div>
                                <div>authorName : {item.authorName}</div>
                                <div>title : {item.title}</div>
                                <div>published : {`${item.published}`}</div>
                                <div>listed : {`${item.listed}`}</div>
                            </Link>
                        </li>
                    ))
                )}
            </ul>

            <hr />

            <div className="flex my-2 gap-2">{pageLinks}</div>
        </div>
    );
}


// "use client";
//
// import type { components } from "@/lib/backend/apiV1/schema";
// import Link from "next/link";
// import { useRouter } from "next/navigation";
//
// export default function ClientPage({
//                                        searchKeyword,
//                                        searchKeywordType,
//                                        page,
//                                        pageSize,
//                                        responseBody,
//                                    }: {
//     searchKeyword: string;
//     searchKeywordType: string;
//     page: number;
//     pageSize: number;
//     responseBody: components["schemas"]["PageDtoPostDto"];
// }) {
//     const router = useRouter();
//
//     return (
//         <div>
//             <form
//                 onSubmit={(e) => {
//                     e.preventDefault();
//
//                     const formData = new FormData(e.target as HTMLFormElement);
//                     const searchKeyword = formData.get("searchKeyword") as string;
//                     const searchKeywordType = formData.get("searchKeywordType") as string;
//                     const page = formData.get("page") as string;
//                     const pageSize = formData.get("pageSize") as string;
//
//                     router.push(
//                         `?page=${page}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`
//                     );
//                 }}
//             >
//                 <input type="hidden" name="page" value="1" />
//                 <select name="pageSize" defaultValue={pageSize}>
//                     <option disabled>페이당 행 수</option>
//                     <option value="10">10</option>
//                     <option value="30">30</option>
//                     <option value="50">50</option>
//                 </select>
//                 <select name="searchKeywordType" defaultValue={searchKeywordType}>
//                     <option disabled>검색어 타입</option>
//                     <option value="title">제목</option>
//                     <option value="content">내용</option>
//                 </select>
//                 <input
//                     placeholder="검색어를 입력해주세요."
//                     type="text"
//                     name="searchKeyword"
//                     defaultValue={searchKeyword}
//                 />
//                 <button type="submit">검색</button>
//             </form>
//
//             <div>
//                 <div>currentPageNumber: {responseBody.pageable.pageNumber}</div>
//                 <div>pageSize: {responseBody.pageable.pageSize}</div>
//                 <div>totalPages: {responseBody.pageable.totalPages}</div>
//                 <div>totalItems: {responseBody.pageable.totalElements}</div>
//             </div>
//
//             <hr />
//
//             <div className="flex my-2 gap-2">
//                 {Array.from({ length: responseBody.pageable.totalPages }, (_, i) => i + 1).map(
//                     (pageNum) => (
//                         <Link
//                             key={pageNum}
//                             className={`px-2 py-1 border rounded ${
//                                 pageNum === responseBody.pageable.pageNumber + 1 ? "text-red-500" : ""
//                             }`}
//                             href={`?page=${pageNum}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
//                         >
//                             {pageNum}
//                         </Link>
//                     )
//                 )}
//             </div>
//
//             <hr />
//
//             <ul>
//                 {responseBody.content.length === 0 ? (
//                     <div>No posts available.</div>
//                 ) : (
//                     responseBody.content.map((item) => (
//                         <li key={item.id} className="border-[2px] border-[red] my-3">
//                             <Link className="block" href={`/post/${item.id}`}>
//                                 <div>id : {item.id}</div>
//                                 <div>createDate : {item.createDate}</div>
//                                 <div>modifyDate : {item.modifyDate}</div>
//                                 <div>authorId : {item.authorId}</div>
//                                 <div>authorName : {item.authorName}</div>
//                                 <div>title : {item.title}</div>
//                                 <div>published : {`${item.published}`}</div>
//                                 <div>listed : {`${item.listed}`}</div>
//                             </Link>
//                         </li>
//                     ))
//                 )}
//             </ul>
//
//             <hr />
//
//             <div className="flex my-2 gap-2">
//                 {Array.from({ length: responseBody.pageable.totalPages }, (_, i) => i + 1).map(
//                     (pageNum) => (
//                         <Link
//                             key={pageNum}
//                             className={`px-2 py-1 border rounded ${
//                                 pageNum === responseBody.pageable.pageNumber + 1 ? "text-red-500" : ""
//                             }`}
//                             href={`?page=${pageNum}&pageSize=${pageSize}&searchKeywordType=${searchKeywordType}&searchKeyword=${searchKeyword}`}
//                         >
//                             {pageNum}
//                         </Link>
//                     )
//                 )}
//             </div>
//         </div>
//     );
// }