"use client";
import {Post} from "@/app/types/Post";
import Link from "next/link";
import {useEffect,useState} from "react";
export default function clientPage(){

    const [posts, setPosts] = useState<Post[]>([]);

    useEffect(()=>{
        fetch("")
            .then((res) => res.json())
            .then((data) => setPosts(data));
    },[]);

    return(
        <>
            <div className="grid">
                {posts.map((post) => (
                    <Link href={`/p/${post.id}`} key={post.id}>
                        {post.title}
                    </Link>
                ))}
            </div>

            <Link href="/p/write">글쓰기</Link>
        </>
    );
}