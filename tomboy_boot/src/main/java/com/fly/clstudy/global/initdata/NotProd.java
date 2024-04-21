package com.fly.clstudy.global.initdata;

import com.fly.clstudy.article.entity.Article;
import com.fly.clstudy.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// !prod == dev or test
@Profile("!prod")
@Configuration
@RequiredArgsConstructor
public class NotProd {
    private final ArticleRepository articleRepository;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            if (articleRepository.count() > 0) return;

            Article article1 = Article.builder()
                    .title("제목 1")
                    .body("내용 2")
                    .build();

            Article article2 = Article.builder()
                    .title("제목 1")
                    .body("내용 2")
                    .build();

            articleRepository.save(article1);
            articleRepository.save(article2);

            articleRepository.delete(article1);
        };
    }
}