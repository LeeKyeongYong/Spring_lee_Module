"use client";

import { components } from "@/lib/backend/apiV1/schema";
import { useRouter } from "next/navigation";

export default function ClientPage({
                                       member,
                                   }: {
    member: components["schemas"]["MemberWithUsernameDto"];
}) {
    const router = useRouter();

    return (
        <div>
            <button onClick={() => router.back()}>뒤로가기</button>
            <hr />
            {member.id}번 회원 상세페이지
            <hr />
            가입날짜 : {member.createDate}
            <hr />
            수정날짜 : {member.modifyDate}
            <hr />
            <h1>{member.username}</h1>
            <hr />
            <p>{member.nickname}</p>
        </div>
    );
}