import axios from 'axios';

const client = axios.create({
    baseURL: process.env.NEXT_PUBLIC_CORE_API_BASE_URL || 'http://localhost:9090/api/v1',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default client;