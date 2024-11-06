"use client";

import { useRouter } from "next/navigation";

export default function ClientPage(){

    const router = useRouter();
    const handleSubmit = async (e:React.FormEvent<HTMLFormElement>) =>{
        e.preventDefault();

        const formData = new FormData(e.target as HTMLFormElement);
        const title = formData.get("title") as string;
        const body= formData.get("body") as string;

        await fetch(`http://localhost:8080/api/v1/posts`,{
            method:"POST",
            body:JSON.stringify({title,body}),
            headers:{
                "Content-Type":"application/json",
            },
        });

        alert("등록되었습니다.");

        router.back();

    };

    return(
        <div>
            <form onSubmit={handleSubmit}>
                <input type="text" name="title" placeholder="제목" />
                <textarea name="body" placeholder="내용"/>
                <button type="submit">등록</button>
            </form>
        </div>
    );
}