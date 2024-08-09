// app/not-found.js
"use client";

// import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import styles from '../pages/404.module.css';
import Notfound from '@/pages/404';
export default function NotFound() {
  const router = useRouter();
//   useEffect(() => {
//     router.replace('/404'); // replace를 사용하여 히스토리를 변경
//   }, [router]);
   return <Notfound/>;
}
