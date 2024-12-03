// member-context.tsx
import { components } from "@/lib/backend/apiV1/schema";
import React, { useState } from "react";

export const MemberContext = React.createContext<{
    loginMember: components["schemas"]["MemberDto"];
    setLoginMember: (member: components["schemas"]["MemberDto"]) => void;
    removeLoginMember: () => void;
    isLogin: boolean;
    isLoginMemberPending: boolean;
}>({
    loginMember: {
        id: 0,
        createDate: "",
        modifyDate: "",
        name: "",
        profileImgUrl: "",
        authorities: [],
        social: false,
    },
    setLoginMember: () => {},
    removeLoginMember: () => {},
    isLogin: false,
    isLoginMemberPending: true,
});

export function useLoginMember() {
    const [isLoginMemberPending, setLoginMemberPending] = useState(true);
    const [loginMember, setLoginMember] = useState<components["schemas"]["MemberDto"]>({
        id: 0,
        createDate: "",
        modifyDate: "",
        username: "",
        profileImgUrl: "",
        authorities: [],
        social: false,
    });

    const removeLoginMember = () => {
        setLoginMember({
            id: 0,
            createDate: "",
            modifyDate: "",
            username: "",
            profileImgUrl: "",
            authorities: [],
            social: false,
        });
        setLoginMemberPending(false);
    };

    const isLogin = loginMember.id !== 0;

    return {
        loginMember,
        removeLoginMember,
        isLogin,
        isLoginMemberPending,
        setLoginMember: (member: components["schemas"]["MemberDto"]) => {
            setLoginMember(member);
            setLoginMemberPending(false);
        },
    };
}