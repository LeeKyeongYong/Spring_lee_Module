import ClientPage from "@/app/p/[id]/ClientPage";

export default function Page({params:{id}}:{params:{id:string}}){
    return <ClientPage id={id} />;
}