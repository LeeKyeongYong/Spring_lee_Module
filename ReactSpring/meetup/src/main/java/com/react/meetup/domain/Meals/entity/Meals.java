package com.react.meetup.domain.Meals.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@Entity
public class Meals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(unique = true)
    private String slug;
    private String image;
    private String summary;
    @Column(columnDefinition = "LONGTEXT")
    private String instructions;
    private String creator;
    @Column(name = "creatorEmail")
    private String creatorEmail;
}