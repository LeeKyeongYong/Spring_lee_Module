// src/util/api.js

export async function fetchMeals() {
  const response = await fetch('http://localhost:8080/api/meals');
  if (!response.ok) {
    throw new Error('Failed to fetch meals');
  }
  return response.json(); // JSON 형태로 반환
}

// util/api.js
export async function fetchMealBySlug(slug) {
  const response = await fetch(`http://localhost:8080/api/meals/${slug}`);
  if (!response.ok) {
    const errorText = await response.text();
    console.error('Fetch failed:', errorText);
    throw new Error('Failed to fetch meal');
  }
  return await response.json();
}


// src/util/api.js

export async function createMeal(meal) {
  try {
    const response = await fetch('http://localhost:8080/api/meals', { // 수정: 상대 URL 사용
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(meal),
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to create meal: ${errorText}`);
    }

    return response.json();
  } catch (error) {
    console.error('Error creating meal:', error);
    throw error;
  }
}
