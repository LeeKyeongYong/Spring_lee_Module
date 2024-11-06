"use client";

import { Post } from "@/types/post";
import { useRouter } from "next/navigation";
import Link from "next/link";
import {useEffect,useState} from "react";
export default function ClientPage({id}:{id:string}){

    const router = useRouter();
    const [post,setPost] = useState<Post | null>(null);

    useEffect(() => {
        fetch(`http://localhost:8080/api/v1/posts/${id}`)
            .then((res) => res.json())
            .then((data) => setPost(data));
    },[]);

    const deletePost = async ()=>{
        await fetch(`http://localhost:8080/api/v1/posts/${id}`,{
            method:"DELETE",
        });
        alert("삭제 되었습니다.");

        router.back();
    }

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
                    <button onClick={deletePost}>삭제</button>
                    <Link href={`/p/${post.id}/edit`}>수정</Link>
                </div>
            )}
        </div>
    );
}