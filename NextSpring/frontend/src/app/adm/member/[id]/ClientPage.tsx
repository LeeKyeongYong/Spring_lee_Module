"use client";

import { components } from "@/lib/backend/apiV1/schema";
import { useRouter } from "next/navigation";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import "dayjs/locale/ko";

// dayjs 설정
dayjs.extend(relativeTime);
dayjs.locale("ko");

function formatDate(dateString: string) {
    const date = dayjs(dateString);
    const now = dayjs();
    const diffMinutes = now.diff(date, "minute");

    if (diffMinutes < 60) {
        return `${date.format("YYYY-MM-DD HH:mm")} (${diffMinutes}분 전)`;
    } else if (diffMinutes < 1440) { // 24시간 이내
        const hours = Math.floor(diffMinutes / 60);
        return `${date.format("YYYY-MM-DD HH:mm")} (${hours}시간 전)`;
    } else if (diffMinutes < 10080) { // 7일 이내
        const days = Math.floor(diffMinutes / 1440);
        return `${date.format("YYYY-MM-DD HH:mm")} (${days}일 전)`;
    } else if (diffMinutes < 43200) { // 30일 이내
        const weeks = Math.floor(diffMinutes / 10080);
        return `${date.format("YYYY-MM-DD HH:mm")} (${weeks}주 전)`;
    } else if (diffMinutes < 525600) { // 1년 이내
        const months = Math.floor(diffMinutes / 43200);
        return `${date.format("YYYY-MM-DD HH:mm")} (${months}개월 전)`;
    } else {
        const years = Math.floor(diffMinutes / 525600);
        return `${date.format("YYYY-MM-DD HH:mm")} (${years}년 전)`;
    }
}

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
            가입날짜 : {formatDate(member.createDate)}
            <hr />
            수정날짜 : {formatDate(member.modifyDate)}
            <hr />
            <h1>{member.username}</h1>
            <hr />
            <p>{member.nickname}</p>
        </div>
    );
}