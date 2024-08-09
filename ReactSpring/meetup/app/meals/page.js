// src/pages/meals/page.js
"use client"; // 클라이언트 컴포넌트로 지정

import { useState, useEffect, Suspense, lazy } from 'react';
import Link from 'next/link';
import { fetchMeals } from '@/util/api'; // 상대 경로 사용
import classes from './page.module.css';
import Loading from './loading-out'; // 로딩 컴포넌트 import

// 동적 import를 사용하여 컴포넌트를 비동기적으로 로드합니다.
const MealsGrid = lazy(() => import('@/components/meals/meals-grid'));

export default function MealsPage() {
  const [meals, setMeals] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {

    console.log('Fetching meals');

    const loadMeals = async () => {
      try {
        // 2초 후에 데이터를 로드
        await new Promise(resolve => setTimeout(resolve, 1000));
        const fetchedMeals = await fetchMeals();
        setMeals(fetchedMeals);
      } catch (error) {
        console.error('Failed to fetch meals', error);
      } finally {
        setLoading(false);
      }
    };

    loadMeals();
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <>
      <header className={classes.header}>
        <h1>
          Delicious meals, created {''}
          <span className={classes.highlight}>by you</span>
        </h1>
        <p>Choose your favorite recipe and cook it yourself. It is easy and fun!</p>
        <p className={classes.cta}>
          <Link href="/meals/about2">Share Your Favorite Recipe</Link>
        </p>
      </header>
      <main className={classes.main}>
        <Suspense fallback={<Loading />}>
          <MealsGrid meals={meals} />
        </Suspense>
      </main>
    </>
  );
}


//버전1
// src/pages/meals/page.js
// "use client";

// import React, { Suspense } from 'react';
// import Link from 'next/link';
// import Meals from '@/components/meals/Meals'; // 수정된 Meals 컴포넌트 import
// import classes from './page.module.css';
// import Loading from './loading-out'; // 로딩 컴포넌트 import

// export default function MealsPage() {
//   return (
//     <>
//       <header className={classes.header}>
//         <h1>
//           Delicious meals, created {''}
//           <span className={classes.highlight}>by you</span>
//         </h1>
//         <p>Choose your favorite recipe and cook it yourself. It is easy and fun!</p>
//         <p className={classes.cta}>
//           <Link href="/meals/share">Share Your Favorite Recipe</Link>
//         </p>
//       </header>
//       <main className={classes.main}>
//         <Suspense fallback={<Loading />}>
//           <Meals />
//         </Suspense>
//       </main>
//     </>
//   );
// }



//버전 0
// // src/pages/meals/page.js
// "use client"; // 클라이언트 컴포넌트로 지정

// import { useState, useEffect } from 'react';
// import Link from 'next/link';
// import MealsGrid from '@/components/meals/meals-grid';
// import { fetchMeals } from '@/util/api'; // 상대 경로 사용
// import classes from './page.module.css';
// import Loading from './loading-out'; // 로딩 컴포넌트 import

// export default function MealsPage() {
//   const [meals, setMeals] = useState([]);
//   const [loading, setLoading] = useState(true);

//   useEffect(() => {
//     const loadMeals = async () => {
//       try {
//         // 2초 후에 데이터를 로드
//         await new Promise(resolve => setTimeout(resolve, 2000));
//         const fetchedMeals = await fetchMeals();
//         setMeals(fetchedMeals);
//       } catch (error) {
//         console.error('Failed to fetch meals', error);
//       } finally {
//         setLoading(false);
//       }
//     };

//     loadMeals();
//   }, []);

//   if (loading) {
//     return <Loading />;
//   }

//   return (
//     <>
//       <header className={classes.header}>
//         <h1>
//           Delicious meals, created {''}
//           <span className={classes.highlight}>by you</span>
//         </h1>
//         <p>Choose your favorite recipe and cook it yourself. It is easy and fun!</p>
//         <p className={classes.cta}>
//           <Link href="/meals/share">Share Your Favorite Recipe</Link>
//         </p>
//       </header>
//       <main className={classes.main}>
//          <MealsGrid meals={meals} />
//       </main>
//     </>
//   );
// }