"use client";
import { useContext, useEffect } from "react";
import { MemberContext } from "@/stores/member";

export default function ClientPage() {
    const { isLogin, loginMember, setLoginMemberPending } = useContext(MemberContext);

    useEffect(() => {
        const checkAuth = async () => {
            const token = localStorage.getItem('accessToken');
            console.log("Access Token:", token); // 토큰 값 확인

            if (!token) {
                console.log("No token found");
                return;
            }

            try {
                setLoginMemberPending(true);

                // API URL 확인
                const apiUrl = `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/me`;
                console.log("API URL:", apiUrl);

                const response = await fetch(apiUrl, {
                    method: 'GET',
                    credentials: 'include',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('accessToken')}`,
                        'Content-Type': 'application/json',
                    }
                });

                console.log("Response status:", response.status);
                console.log("Response headers:", [...response.headers.entries()]);

                if (response.status === 401) {
                    console.error("Unauthorized: Token might be invalid or expired.");
                    return; // 토큰 문제로 401 오류 발생 시 처리
                }

                if (!response.ok) {
                    throw new Error(`Auth check failed: ${response.status}`);
                }

                const data = await response.json();
                console.log("Auth response:", data);
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
            <p>{loginMember?.username}</p>
            <p>{loginMember?.nickname}</p>
        </div>
    );
}
