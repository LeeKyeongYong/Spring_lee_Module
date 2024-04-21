package com.fly.clstudy.article.repository;

import com.fly.clstudy.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}