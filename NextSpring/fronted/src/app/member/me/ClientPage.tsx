"use client";

import { useContext, useEffect } from "react";
import { MemberContext } from "@/stores/member";

export default function ClientPage() {
    const { isLogin, loginMember, setLoginMemberPending } = useContext(MemberContext);

    useEffect(() => {
        const checkAuth = async () => {
            const token = localStorage.getItem('accessToken');
            console.log("Access Token:", token);

            if (!token) {
                console.log("No token found");
                return;
            }

            try {
                setLoginMemberPending(true);
                const response = await fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/me`, {
                    method: 'GET',
                    credentials: 'include',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if (!response.ok) {
                    throw new Error(`Auth check failed: ${response.status}`);
                }

                const data = await response.json();
                console.log("Auth response:", data);
                // 데이터 처리
            } catch (error) {
                console.error('Auth check error:', error);
            } finally {
                setLoginMemberPending(false);
            }
        };

        checkAuth();
    }, [setLoginMemberPending]);

    if (!isLogin) return <div>로그인 상태가 아닙니다.</div>;

    return (
        <div>
            <h1>me</h1>
            <p>{loginMember?.name}</p>
        </div>
    );
}