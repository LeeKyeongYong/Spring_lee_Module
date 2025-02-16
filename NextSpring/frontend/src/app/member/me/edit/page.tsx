import { cookies } from "next/headers";
import ClientPage from "./ClientPage";
import { parseAccessToken } from "@/lib/auth/tokens";

export default async function Page() {
    const cookieStore = await cookies();
    const accessToken = cookieStore.get("accessToken")?.value;

    const { me } = parseAccessToken(accessToken);

    return <ClientPage me={me} />;
}