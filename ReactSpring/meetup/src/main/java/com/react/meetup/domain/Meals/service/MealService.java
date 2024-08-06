package com.react.meetup.domain.Meals.service;

import com.react.meetup.domain.Meals.entity.Meals;
import com.react.meetup.domain.Meals.repository.MealRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NonUniqueResultException;
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
//        return mealRepository.findBySlug(slug);
        List<Meals> meals = mealRepository.findBySlug(slug);
        if (meals.isEmpty()) {
            throw new EntityNotFoundException("Meal not found for slug: " + slug);
        } else if (meals.size() > 1) {
            // Handle multiple results scenario or throw a custom exception
            throw new NonUniqueResultException("Multiple meals found for slug: " + slug);
        }
        return meals.get(0);

    }

    public Meals saveMeal(Meals meal) {
        return mealRepository.save(meal);
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }
}
