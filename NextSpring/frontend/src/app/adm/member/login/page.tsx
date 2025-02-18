import RequireAnonymous from "@/lib/auth/components/RequireAnonymous";
import ClientPage from "./ClientPage";

export default function Page() {
    return (
        <RequireAnonymous>
            <ClientPage />
        </RequireAnonymous>
    );
}