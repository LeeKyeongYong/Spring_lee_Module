import ClientPage from "./ClientPage";
import { Metadata } from "next";
import { cookies } from "next/headers";

export const metadata: Metadata = {};

export default async function Page({ params }:{params:{id:string}}){

    const { id } = await params;

    // 쿠키 스토어에서 쿠키들을 가져옴
    const response = await fetch(
        `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/posts/${id}`,
        {
            credentials: "include",
            method: "GET",
            headers: {
                Cookie: (await cookies()).toString(),
            },
        }
    );

    if (!response.ok) {
        return <div>권한이 없습니다.</div>;
    }

    const post = await response.json();

    // seo 를 위한 meta tag 작업
    const title = post.title;
    const description = post.content;

    metadata.title = title;
    metadata.description = description;

    return <ClientPage id={id} post={post} />;
}