// src/components/meals/Meals.js
import React, { Suspense, lazy } from 'react';
import Loading from '@/app/meals/loading-out'; // 로딩 컴포넌트 import
import { fetchMeals } from '@/util/api'; // api.js에서 fetchMeals 함수 import
import Error from '@/pages/_error'; // 에러 페이지 import

const MealsGrid = lazy(() => import('@/components/meals/meals-grid'));

const Meals = () => {
  const [meals, setMeals] = React.useState([]);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null); // 에러 상태 추가

  React.useEffect(() => {
    const loadMeals = async () => {
      try {
        // 2초 지연 추가
        await new Promise(resolve => setTimeout(resolve, 2000));
        const fetchedMeals = await fetchMeals(); // fetchMeals 호출
        setMeals(fetchedMeals);
      } catch (error) {
        console.error('Failed to fetch meals', error);
        setError(error); // 에러 상태 설정
      } finally {
        setLoading(false);
      }
    };

    loadMeals();
  }, []); // 빈 배열로 한 번만 호출되도록 설정

  if (loading) {
    return <Loading />;
  }

  if (error) {
    return <Error error={error} />; // 에러 페이지 반환
  }

  return (
    <Suspense fallback={<Loading />}>
      <MealsGrid meals={meals} />
    </Suspense>
  );
};

export default Meals;


// part4
// import React, { Suspense, lazy } from 'react';
// import Loading from '@/app/meals/loading-out'; // 로딩 컴포넌트 import
// import { fetchMeals } from '@/util/api'; // api.js에서 fetchMeals 함수 import
// import Error from '@/pages/_error'; // 에러 페이지 import

// const MealsGrid = lazy(() => import('@/components/meals/meals-grid'));

// const Meals = () => {
//   const [meals, setMeals] = React.useState([]);
//   const [loading, setLoading] = React.useState(true);
//   const [error, setError] = React.useState(null); // 에러 상태 추가

//   React.useEffect(() => {
//     const loadMeals = async () => {
//       try {
//         // 2초 지연 추가
//         await new Promise(resolve => setTimeout(resolve, 2000));
//         const fetchedMeals = await fetchMeals(); // fetchMeals 호출
//         setMeals(fetchedMeals);
//       } catch (error) {
//         console.error('Failed to fetch meals', error);
//         setError(error); // 에러 상태 설정
//       } finally {
//         setLoading(false);
//       }
//     };

//     loadMeals();
//   }, []); // 빈 배열로 한 번만 호출되도록 설정

//   if (loading) {
//     return <Loading />;
//   }

//   if (error) {
//     return <Error error={error} />; // 에러 페이지 반환
//   }

//   return (
//     <Suspense fallback={<Loading />}>
//       <MealsGrid meals={meals} />
//     </Suspense>
//   );
// };

// export default Meals;


// part3
// import React, { Suspense, lazy } from 'react';
// import Loading from '@/app/meals/loading-out'; // 로딩 컴포넌트 import
// import { fetchMeals } from '@/util/api'; // api.js에서 fetchMeals 함수 import
// import Error from '@/pages/_error'; // 에러 페이지 import

// const MealsGrid = lazy(() => import('@/components/meals/meals-grid'));

// const Meals = () => {
//   const [meals, setMeals] = React.useState([]);
//   const [loading, setLoading] = React.useState(true);
//   const [error, setError] = React.useState(null); // 에러 상태 추가

//   React.useEffect(() => {
//     const loadMeals = async () => {
//       try {
//         // 2초 지연 추가
//         await new Promise(resolve => setTimeout(resolve, 2000));
//         const fetchedMeals = await fetchMeals(); // fetchMeals 호출
//         setMeals(fetchedMeals);
//       } catch (error) {
//         console.error('Failed to fetch meals', error);
//         setError(error); // 에러 상태 설정
//       } finally {
//         setLoading(false);
//       }
//     };

//     loadMeals();
//   }, []); // 빈 배열로 한 번만 호출되도록 설정

//   if (loading) {
//     return <Loading />;
//   }

//   if (error) {
//     return <Error error={error} />; // 에러 페이지 반환
//   }

//   return (
//     <Suspense fallback={<Loading />}>
//       <MealsGrid meals={meals} />
//     </Suspense>
//   );
// };

// export default Meals;






//part2
// import React, { Suspense, lazy } from 'react';
// import Loading from '@/app/meals/loading-out'; // 로딩 컴포넌트 import
// import { fetchMeals } from '@/util/api'; // api.js에서 fetchMeals 함수 import
// import ErrorBoundary from '@/components/ErrorBoundary'; // ErrorBoundary import

// const MealsGrid = lazy(() => import('@/components/meals/meals-grid'));

// const Meals = () => {
//   const [meals, setMeals] = React.useState([]);
//   const [loading, setLoading] = React.useState(true);

//   React.useEffect(() => {
//     const loadMeals = async () => {
//       try {
//         // 2초 지연 추가
//         await new Promise(resolve => setTimeout(resolve, 2000));
//         const fetchedMeals = await fetchMeals(); // fetchMeals 호출
//         setMeals(fetchedMeals);
//       } catch (error) {
//         console.error('Failed to fetch meals', error);
//         // 상태를 에러로 변경하고 ErrorBoundary가 에러를 처리하도록 할 수 있음
//         throw error; 
//       } finally {
//         setLoading(false);
//       }
//     };

//     loadMeals();
//   }, []); // 빈 배열로 한 번만 호출되도록 설정

//   if (loading) {
//     return <Loading />;
//   }

//   return (
//     <ErrorBoundary>
//       <Suspense fallback={<Loading />}>
//         <MealsGrid meals={meals} />
//       </Suspense>
//     </ErrorBoundary>
//   );
// };

// export default Meals;



//part1
/*
import React, { Suspense, lazy } from 'react';
import Loading from '@/app/meals/loading-out'; // 로딩 컴포넌트 import
import { fetchMeals } from '@/util/api'; // api.js에서 fetchMeals 함수 import

const MealsGrid = lazy(() => import('@/components/meals/meals-grid'));

const Meals = () => {
  const [meals, setMeals] = React.useState([]);
  const [loading, setLoading] = React.useState(true);

  React.useEffect(() => {
    const loadMeals = async () => {
      try {
        // 2초 지연 추가
        await new Promise(resolve => setTimeout(resolve,2000));
        const fetchedMeals = await fetchMeals(); // fetchMeals 호출
        setMeals(fetchedMeals);
      } catch (error) {
        console.error('Failed to fetch meals', error);
      } finally {
        setLoading(false);
      }
    };

    loadMeals();
  }, []); // 빈 배열로 한 번만 호출되도록 설정

  if (loading) {
    return <Loading />;
  }

  return (
    <Suspense fallback={<Loading />}>
      <MealsGrid meals={meals} />
    </Suspense>
  );
};

export default Meals;
*/
