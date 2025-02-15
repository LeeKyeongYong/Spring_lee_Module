"use client";

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
        </div>
    );
}