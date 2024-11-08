import { useState, useEffect } from "react";

// 로그인 상태를 확인하고, 토큰을 가져오는 커스텀 훅
export function useAuth() {
    const [accessToken, setAccessToken] = useState<string | null>(null);
    const [isInitialized, setIsInitialized] = useState(false);

    // 로그인 여부를 체크하는 함수
    const isLoggedIn = () => {
        return !!accessToken && isInitialized;
    };

    // 토큰을 가져오는 함수
    const getAccessToken = async () => {
        if (!accessToken) {
            const token = localStorage.getItem("accessToken");
            setAccessToken(token);
            return token || "";
        }
        return accessToken;
    };

    // 페이지가 로드될 때, 저장된 토큰을 읽어오는 로직
    useEffect(() => {
        const token = localStorage.getItem("accessToken");  // 또는 sessionStorage
        if (token) {
            setAccessToken(token);
        }
        setIsInitialized(true);  // 초기화 상태를 true로 설정
    }, []);

    return { getAccessToken, isLoggedIn };
}