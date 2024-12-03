import createClient from "openapi-fetch";
import type { paths } from "@/lib/backend/apiV1/schema";
type Client = ReturnType<typeof createClient<paths>>;

const client: Client = createClient<paths>({
    baseUrl: "http://localhost:8080",
    credentials: "include",
});

export default client;