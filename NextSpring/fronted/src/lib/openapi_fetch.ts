import createClient from "openapi-fetch";
import type { paths } from "@/lib/backend/apiV1/schema";

type Client = ReturnType<typeof createClient<paths>>;

const client: Client = createClient<paths>({
    baseURL: process.env.NEXT_PUBLIC_CORE_API_BASE_URL || 'http://localhost:9090/api/v1',
    credentials: "include", // 쿠키를 포함해 요청을 보냄
});

export default client;
