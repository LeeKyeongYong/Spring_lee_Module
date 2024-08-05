package com.react.meetup.domain.Meals.repository;

import com.react.meetup.domain.Meals.entity.Meals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meals, Long> {
    Meals findBySlug(String slug);
}