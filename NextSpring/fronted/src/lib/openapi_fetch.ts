import createClient from "openapi-fetch";
import type { paths } from "@/lib/backend/apiV1/schema";

type Client = ReturnType<typeof createClient<paths>>;

// API 경로에 /api/v1 추가
const baseUrl = process.env.NEXT_PUBLIC_CORE_API_BASE_URL || 'http://localhost:9090/api/v1';

const client: Client = createClient<paths>({
    baseUrl: baseUrl,
    credentials: "include",
});

export default client;