package com.fly.clstudy.domain.article.repository;

import com.fly.clstudy.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> { }