import type { paths } from "@/lib/backend/apiV1/schema";
import createClient from "openapi-fetch";

const client = createClient<paths>({
    baseUrl: "http://localhost:9090",
    credentials: "include",
    headers: {
        'Content-Type': 'application/json'
    }
});

export default client;