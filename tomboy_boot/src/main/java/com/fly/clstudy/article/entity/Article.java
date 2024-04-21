package com.fly.clstudy.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Getter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String body;
}