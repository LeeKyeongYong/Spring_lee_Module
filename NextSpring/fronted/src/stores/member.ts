import { createContext, useEffect, useState } from 'react';
import api from '@/lib/axios';
import axios from 'axios';

interface Member {
    id: number;
    username: string;
    nickname: string;
}

interface MemberContextType {
    loginMember: Member | null;
    setLoginMember: (member: Member | null) => void;
    removeLoginMember: () => void;
    isLogin: boolean;
    isLoginMemberPending: boolean;
}

export const MemberContext = createContext<MemberContextType>({
    loginMember: null,
    setLoginMember: () => {},
    removeLoginMember: () => {},
    isLogin: false,
    isLoginMemberPending: true,
});

export function useLoginMember() {
    const [loginMember, setLoginMember] = useState<Member | null>(null);
    const [isLoginMemberPending, setIsLoginMemberPending] = useState(true);

    useEffect(() => {
        fetchLoginMember();
    }, []);

    const fetchLoginMember = async () => {
        try {
            const response = await api.get('/members/me');

            if (response.status === 200 && response.data) {
                setLoginMember(response.data.data);
            }
        } catch (error) {
            if (axios.isAxiosError(error)) {
                if (error.response?.status === 403) {
                    // 로그인되지 않은 상태는 정상적인 케이스로 처리
                    console.log('User not logged in - This is normal for non-authenticated users');
                    setLoginMember(null);  // 명시적으로 null 설정
                } else {
                    console.error('API error:', {
                        status: error.response?.status,
                        data: error.response?.data,
                        message: error.message
                    });
                }
            } else {
                console.error('Unexpected error:', error);
            }
        } finally {
            setIsLoginMemberPending(false);
        }
    };

    const removeLoginMember = () => {
        setLoginMember(null);
    };

    return {
        loginMember,
        setLoginMember,
        removeLoginMember,
        isLogin: !!loginMember,
        isLoginMemberPending,
    };
}