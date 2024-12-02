"use client";

import React, { useContext, useState } from "react";
import { useRouter } from "next/navigation";
import { MemberContext } from "@/stores/member";
import type { FormEvent } from "react";

interface LoginResponse {
    data: {
        item: {
            id: number;
            username: string;
            nickname: string;
        };
    };
    error?: {
        msg: string;
    };
}

export default function LoginPage() {
    const router = useRouter();
    const { setLoginMember, isLogin } = useContext(MemberContext);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    if (isLogin) {
        return <div className="text-center p-4">이미 로그인 상태입니다.</div>;
    }

    const handleSubmit = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setIsLoading(true);

        try {
            const form = e.currentTarget;
            const formData = new FormData(form);
            const username = formData.get("username") as string;
            const password = formData.get("password") as string;

            const response = await fetch(`${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username,
                    password,
                }),
                credentials: 'include',
            });

            const data = await response.json() as LoginResponse;

            if (!response.ok || data.error) {
                throw new Error(data.error?.msg || '로그인에 실패했습니다.');
            }

            setLoginMember(data.data.item);
            router.push("/");

        } catch (error) {
            console.error('Login error:', error);
            alert(error instanceof Error ? error.message : '로그인 중 오류가 발생했습니다.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="flex justify-center items-center min-h-[50vh]">
            <form onSubmit={handleSubmit} className="w-full max-w-md space-y-4 p-4">
                <div className="space-y-4">
                    <div>
                        <label
                            htmlFor="username"
                            className="block text-sm font-medium text-gray-700"
                        >
                            아이디
                        </label>
                        <input
                            id="username"
                            name="username"
                            type="text"
                            required
                            placeholder="아이디를 입력하세요"
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                            disabled={isLoading}
                        />
                    </div>
                    <div>
                        <label
                            htmlFor="password"
                            className="block text-sm font-medium text-gray-700"
                        >
                            비밀번호
                        </label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            required
                            placeholder="비밀번호를 입력하세요"
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                            disabled={isLoading}
                        />
                    </div>
                </div>
                <button
                    type="submit"
                    className="w-full flex justify-center py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                    disabled={isLoading}
                >
                    {isLoading ? '로그인 중...' : '로그인'}
                </button>
            </form>
        </div>
    );
}