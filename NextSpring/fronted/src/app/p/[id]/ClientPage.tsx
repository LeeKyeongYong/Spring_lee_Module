"use client";

import { Post } from "@/app/types/Post";
import { useRouter } from "next/navigation";

import {useEffect,useState} from "react";
export default function ClientPage({id}:{id:string}){

    const router = useRouter();
    const [post,setPost] = useState<Post | null>(null);

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/posts/${id}`)
            .then((res) => res.json())
            .then((data) => setPost(data));
    },[]);

    return(
        <div className="grid">
            {post && (
                <div>
                    <div>{post.id}</div>
                    <div>{post.title}</div>
                    <div>{post.createDate}</div>
                    <div>{post.modifyDate}</div>
                    <div>{post.body}</div>
                    <button onClick={() => router.back()}>뒤로가기</button>
                </div>
            )}
        </div>
    );
}