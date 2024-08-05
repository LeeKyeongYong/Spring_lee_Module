package com.react.meetup.domain.Meals.service;

import com.react.meetup.domain.Meals.entity.Meals;
import com.react.meetup.domain.Meals.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {
    @Autowired
    private MealRepository mealRepository;

    public List<Meals> getAllMeals() {
        return mealRepository.findAll();
    }

    public Meals getMealBySlug(String slug) {
        return mealRepository.findBySlug(slug);
    }

    public Meals saveMeal(Meals meal) {
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }
}
