
import { Member } from "@/types/member";
import React, { useState } from "react";

export const MemberContext = React.createContext<{
    loginMember: Member;
    setLoginMember: React.Dispatch<React.SetStateAction<Member>>;
    removeLoginMember: () => void;
    isLogin: boolean;
}>({
    loginMember: createEmptyMember(),
    setLoginMember: () => {},
    removeLoginMember: () => {},
    isLogin: false,
});

function createEmptyMember(): Member {
    return {
        id: 0,
        createDate: "",
        modifyDate: "",
        username: "",
        password: "",
        name: "",
    };
}

export function useLoginMember() {
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
    };
}