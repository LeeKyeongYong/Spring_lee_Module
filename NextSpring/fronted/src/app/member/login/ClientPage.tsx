"use client";

import axios from 'axios';
import { useState, useContext } from "react";
import { MemberContext } from "@/stores/member";
import { useRouter } from "next/navigation";

export default function ClientPage() {
    const router = useRouter();
    const { setLoginMember } = useContext(MemberContext);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const apiUrl = `${process.env.NEXT_PUBLIC_CORE_API_BASE_URL}/members/login`;
        console.log('API URL:', apiUrl);
        console.log('Request body:', { username, password });

        try {
            const response = await axios.post(apiUrl,
                { username, password },
                {
                    withCredentials: true,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            );

            // axios는 response.ok 대신 status로 확인합니다
            console.log('Success response:', response.data);

            setLoginMember(response.data.data.item);
            localStorage.setItem("accessToken", response.data.accessToken);
            alert("로그인되었습니다.");
            router.push("/");
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.error('Error response:', error.response?.data);
                alert(error.response?.data?.message || "로그인에 실패했습니다.");
            } else {
                console.error("로그인 실패:", error);
                alert("로그인에 실패했습니다.");
            }
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
            <div className="max-w-md w-full space-y-8">
                <div>
                    <h2 className="mt-6 text-center text-3xl font-bold text-gray-900">
                        로그인
                    </h2>
                </div>
                <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
                    <div className="rounded-md shadow-sm -space-y-px">
                        <div>
                            <input
                                name="username"
                                type="text"
                                required
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                className="appearance-none rounded-none block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                placeholder="아이디"
                            />
                        </div>
                        <div>
                            <input
                                name="password"
                                type="password"
                                required
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className="appearance-none rounded-none block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-b-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
                                placeholder="비밀번호"
                            />
                        </div>
                    </div>

                    <div>
                        <button
                            type="submit"
                            className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                        >
                            로그인
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}