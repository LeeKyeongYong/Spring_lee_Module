package com.react.meetup.domain.Meals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealsDTO {
    private Long id;
    private String title;
    private String slug;
    private String image;
    private String summary;
    private String instructions;
    private String creator;
    private String creatorEmail;
}
