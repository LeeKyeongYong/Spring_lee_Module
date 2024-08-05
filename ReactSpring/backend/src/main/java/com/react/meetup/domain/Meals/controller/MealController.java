package com.react.meetup.domain.Meals.controller;

import com.react.meetup.domain.Meals.dto.MealsDTO;
import com.react.meetup.domain.Meals.entity.Meals;
import com.react.meetup.domain.Meals.service.MealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meals")
public class MealController {
    @Autowired
    private MealService mealService;

    @GetMapping
    public List<Meals> getAllMeals() {
        List<Meals> mealsList = mealService.getAllMeals();
        Logger logger = LoggerFactory.getLogger(MealController.class);
        logger.info("결과: {}", mealsList);
        System.out.println("결과: "+mealService.getAllMeals().toString());
        return mealsList;
    }

//    @GetMapping
//    public List<MealsDTO> getAllMeals() {
//        List<Meals> mealsList = mealService.getAllMeals();
//        return mealsList.stream()
//                .map(meal -> new MealsDTO(
//                        meal.getId(),
//                        meal.getTitle(),
//                        meal.getSlug(),
//                        meal.getImage(),
//                        meal.getSummary(),
//                        meal.getInstructions(),
//                        meal.getCreator(),
//                        meal.getCreatorEmail()
//                ))
//                .collect(Collectors.toList());
//    }

    @GetMapping("/{slug}")
    public ResponseEntity<Meals> getMealBySlug(@PathVariable("slug") String slug) {
        Meals meal = mealService.getMealBySlug(slug);
        if (meal != null) {
            return ResponseEntity.ok(meal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Meals createMeal(@RequestBody Meals meal) {
        return mealService.saveMeal(meal);
    }

    @PutMapping("/{id}")
    public Meals updateMeal(@PathVariable Long id, @RequestBody Meals meal) {
        meal.setId(id);
        return mealService.saveMeal(meal);
    }

    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
    }
}
