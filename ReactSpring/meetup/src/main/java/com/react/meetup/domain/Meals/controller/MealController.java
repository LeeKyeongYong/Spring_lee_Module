package com.react.meetup.domain.Meals.controller;

import com.react.meetup.domain.Meals.dto.MealsDTO;
import com.react.meetup.domain.Meals.entity.Meals;
import com.react.meetup.domain.Meals.service.MealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meals")
public class MealController {
    @Autowired
    private MealService mealService;

    @GetMapping
    public List<MealsDTO> getAllMeals() {
        List<Meals> mealsList = mealService.getAllMeals();
        return mealsList.stream()
                .map(meal -> new MealsDTO(
                        meal.getId(),
                        meal.getTitle(),
                        meal.getSlug(),
                        meal.getImage(),
                        meal.getSummary(),
                        meal.getInstructions(),
                        meal.getCreator(),
                        meal.getCreatorEmail()
                ))
                .collect(Collectors.toList());
    }
    @GetMapping("/{slug}")
    public ResponseEntity<MealsDTO> getMealBySlug(@PathVariable("slug") String slug) {
        Meals meal = mealService.getMealBySlug(slug);
        if (meal != null) {
            MealsDTO mealDTO = new MealsDTO(
                    meal.getId(),
                    meal.getTitle(),
                    meal.getSlug(),
                    meal.getImage(),
                    meal.getSummary(),
                    meal.getInstructions(),
                    meal.getCreator(),
                    meal.getCreatorEmail()
            );
            return ResponseEntity.ok(mealDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<MealsDTO> createMeal(@RequestBody Meals meal) {
        String imageName = meal.getImage();

        // 파일명을 UTF-8로 디코딩
        String decodedImageName;
        try {
            decodedImageName = URLDecoder.decode(imageName, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            decodedImageName = imageName;
            System.err.println("Error decoding image name: " + e.getMessage());
        }

        Meals savedMeal = mealService.saveMeal(meal);

        // Meals 객체를 MealsDTO로 변환
        MealsDTO mealDTO = new MealsDTO(
                savedMeal.getId(),
                savedMeal.getTitle(),
                savedMeal.getSlug(),
                savedMeal.getImage(),
                savedMeal.getSummary(),
                savedMeal.getInstructions(),
                savedMeal.getCreator(),
                savedMeal.getCreatorEmail()
        );

        return ResponseEntity.ok(mealDTO);
    }

    @PutMapping("/{id}")
    public MealsDTO updateMeal(@PathVariable Long id, @RequestBody Meals meal) {
        meal.setId(id);
        Meals updatedMeal = mealService.saveMeal(meal);

        // Meals 객체를 MealsDTO로 변환
        return new MealsDTO(
                updatedMeal.getId(),
                updatedMeal.getTitle(),
                updatedMeal.getSlug(),
                updatedMeal.getImage(),
                updatedMeal.getSummary(),
                updatedMeal.getInstructions(),
                updatedMeal.getCreator(),
                updatedMeal.getCreatorEmail()
        );
    }

    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
    }

}
