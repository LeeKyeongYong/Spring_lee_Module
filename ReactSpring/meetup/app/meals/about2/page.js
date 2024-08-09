'use client';

import { useRouter } from 'next/navigation';
import classes from './page.module.css';
import ImagePicker from '@/components/meals/image-picker';
import { createMeal } from '@/util/api';
import MealsFormSubmit from '../meals-form-submit';
import { useState } from 'react';
import { revalidatePath } from 'next/cache';

function isInValidTest(text){
  return !text || text ==='';
}

export default function ShareMealPage() {

  const [errorMessage, setErrorMessage] = useState(null);

  const router = useRouter();

  // 파일 업로드 함수 (예: React 컴포넌트 내)
// 'shareMeal' 함수 내에서
async function shareMeal(event) {
  event.preventDefault();

  const formData = new FormData(event.target);
  
  // 'image' 파일의 경우, 파일명 인코딩을 처리할 수 있는 별도의 로직을 추가
  const file = formData.get('image');
  if (file) {
    const encodedFileName = encodeURIComponent(file.name);
    formData.set('image', new File([file], encodedFileName, { type: file.type }));
  }

  try {
    const uploadResponse = await fetch('/api/upload', {
      method: 'POST',
      body: formData,
    });

    if (!uploadResponse.ok) {
      throw new Error('Image upload failed');
    }

    const { fileUrl } = await uploadResponse.json();
    const title = formData.get('title');
    const slug = title ? title.trim().replace(/\s+/g, '-').toLowerCase() : '';

    const meal = {
      
      title: title,
      summary: formData.get('summary'),
      instructions: formData.get('instructions'),
      image: fileUrl,
      creator: formData.get('name'),
      creatorEmail: formData.get('email'),
      slug: slug,
    };

  
    //유효성 검사
    if(
      isInValidTest(meal.title)||
      isInValidTest(meal.summary)||
      isInValidTest(meal.instructions)||
      isInValidTest(meal.creator)||
      isInValidTest(meal.creatorEmail)||
      !meal.creatorEmail.includes('@')||
      !meal.image||meal.image.size === 0||
      isInValidTest(meal.slug)){
           setErrorMessage('Invalid input.');
        return;
    }


    await createMeal(meal);
    revalidatePath('/meals','layout');

    console.log('Meal submitted successfully');
    router.push('/meals');  // 적절한 리다이렉트 경로로 수정해주세요
  } catch (error) {
    console.error('Error:', error);
    alert('Failed to submit meal. Please try again.');
  }
}




  return (
    <>
      <header className={classes.header}>
        <h1>
          Share your <span className={classes.highlight}>favorite meal</span>
        </h1>
        <p>Or any other meal you feel needs sharing!</p>
      </header>
      <main className={classes.main}>
        <form className={classes.form} onSubmit={shareMeal}>
          <div className={classes.row}>
            <p>
              <label htmlFor="name">Your name</label>
              <input type="text" id="name" name="name" required />
            </p>
            <p>
              <label htmlFor="email">Your email</label>
              <input type="email" id="email" name="email" required />
            </p>
          </div>
          <p>
            <label htmlFor="title">Title</label>
            <input type="text" id="title" name="title" required />
          </p>
          <p>
            <label htmlFor="summary">Short Summary</label>
            <input type="text" id="summary" name="summary" required />
          </p>
          <p>
            <label htmlFor="instructions">Instructions</label>
            <textarea
              id="instructions"
              name="instructions"
              rows="10"
              required
            ></textarea>
          </p>
          <ImagePicker label={"Your image"} name="image" />
          {errorMessage && (
            <p className={classes.error}>{errorMessage}</p>
          )}
          <p className={classes.actions}>
           <MealsFormSubmit />
          </p>
        </form>
      </main>
    </>
  );
}