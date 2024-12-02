import axios from 'axios';

const api = axios.create({
    baseURL: process.env.NEXT_PUBLIC_CORE_API_BASE_URL || 'http://localhost:9090/api/v1',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

// 응답 인터셉터 추가
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 403) {
            console.log('Authentication required');
            // 로그인 페이지로 리다이렉트하거나 다른 처리
        }
        return Promise.reject(error);
    }
);

export default api;