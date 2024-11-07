import { Member } from "@/types/member";
import React, { useState } from "react";

export const MemberContext = React.createContext<{
    loginMember: Member;
    setLoginMember: React.Dispatch<React.SetStateAction<Member>>;
    removeLoginMember: () => void;
    isLogin: boolean;
    isLoginMemberPending: boolean;
    setLoginMemberPending: React.Dispatch<React.SetStateAction<boolean>>;
}>({
    loginMember: createEmptyMember(),
    setLoginMember: () => {},
    removeLoginMember: () => {},
    isLogin: false,
    isLoginMemberPending: true,
    setLoginMemberPending: () => {},
});

function createEmptyMember(): Member {
    return {
        id: 0,
        createDate: "",
        modifyDate: "",
        name: "",
    };
}

export function useLoginMember() {
    const [isLoginMemberPending, setLoginMemberPending] = useState(true);
    const [loginMember, setLoginMember] = useState<Member>(createEmptyMember());

    const removeLoginMember = () => {
        setLoginMember(createEmptyMember());
    };

    const isLogin = loginMember.id !== 0;

    return {
        loginMember,
        setLoginMember,
        removeLoginMember,
        isLogin,
        isLoginMemberPending,
        setLoginMemberPending,
    };
}