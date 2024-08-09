// app/meals/[mealslug]/page.js
import { notFound } from 'next/navigation';
import Image from 'next/image';
import Link from 'next/link';
import { fetchMealBySlug } from '@/util/api'; // API 유틸리티에서 데이터 패칭 함수
import classes from './page.module.css'; // CSS 모듈 import


export async function generateMetadata({ params }) {
  const { mealslug } = params;
  try {
    const meal = await fetchMealBySlug(mealslug);
    if (!meal) {
      return { title: 'Meal Not Found' };
    }
    return { title: meal.title };
  } catch (error) {
    console.error('Failed to fetch meal for metadata:', error);
    return { title: 'Meal Not Found' };
  }
}

export default async function MealDetailPage({ params }) {
  const { mealslug } = params;
  try {
    const meal = await fetchMealBySlug(mealslug);
    meal.instructions = meal.instructions.replace(/\n/g, `<br />`);
    return (
      <>
        <header className={classes.header}>
          <div className={classes.image}>
            <Image
              src={meal.image}
              alt={meal.title}
              width={500}
              height={300}
              style={{ maxWidth: '100%' }}
            />
          </div>
          <div className={classes.headerText}>
            <h1>{meal.title}</h1>
            <p className={classes.creator}>
              by <Link href={`mailto:${meal.creatorEmail}`}>{meal.creator}</Link>
            </p>
            <p className={classes.summary}>{meal.summary}</p>
          </div>
        </header>
        <main>
          <p className={classes.instructions} dangerouslySetInnerHTML={{ __html: meal.instructions }}></p>
        </main>
      </>
    );
  } catch (error) {
    console.error('Failed to fetch meal:', error);
    notFound(); // 에러 발생 시 404 페이지를 렌더링
  }
}
