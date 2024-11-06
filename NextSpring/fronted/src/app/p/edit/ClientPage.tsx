"use client";

import {Post} from "@/app/types/Post";
import Link from "next/link";
import { useRouter } from "next/navigation";

import {useEffect,useState} from "react";
export default function ClientPage({id}:{id:string}){

    const router = useRouter();
    const[post,setPost] = useState<Post | null>(null);

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/posts/${id}`)
            .then((res) => res.json())
            .then((data) => setPost(data));
    },[]);

    const handleSubmit = async (e:React.FormEvent<HTMLFormElement>)=>{
        e.preventDefault();

        const formData = new FormData(e.target as HTMLFormElement);
        const title = formData.get("title") as string;
        const body = formData.get("body") as string;

        await fetch(`http://localhost:8080/api/v1/posts/${id}`,{
            method:"PUT",
            body:JSON.stringify({title,body}),
            headers:{
                "Content-Type":"application/json",
            }
        });

        alert("수정 되었습니다.");
        router.back();
    };

    return(
        <div>
            {post &&(
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="title"
                        placeholder="제목"
                        defaultValue={post.title}
                    />
                    <textarea name="body" placeholder="내용" defaultValue={post.body} />
                    <button type="submit">수정</button>
                </form>
            )}
        </div>
    );
}