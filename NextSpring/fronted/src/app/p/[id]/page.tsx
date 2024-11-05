import ClientPage from "@/app/p/[id]/ClientPage";

export default async function Page({ params }:{params:{id:string}}){

    const { id } = await params;

    return <ClientPage id={id} />;
}