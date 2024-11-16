import createClient from "openapi-fetch";
import type { paths } from "@/lib/backend/apiV1/schema";
type Client = ReturnType<typeof createClient<paths>>;

const client: Client = createClient<paths>({
    baseUrl:  process.env.NEXT_PUBLIC_CORE_API_BASE_URL,
    credentials: "include",
});

export default client;
