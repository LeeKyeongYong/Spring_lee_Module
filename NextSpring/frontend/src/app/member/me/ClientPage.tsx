"use client";
import Link from "next/link";
import { components } from "@/lib/backend/apiV1/schema";

export default function ClientPage({
                                       me,
                                   }: {
    me: components["schemas"]["MemberDto"];
}) {
    return (
        <div>
            <div>ID : {me.id}</div>
            <div>별명 : {me.nickname}</div>
            <Link href="me/edit">회원정보 수정</Link>
        </div>
    );
}