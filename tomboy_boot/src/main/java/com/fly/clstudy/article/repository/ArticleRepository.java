package com.fly.clstudy.article.repository;

import com.fly.clstudy.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> { }