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
            System.out.println("NotProd.initNotProd1");
            System.out.println("NotProd.initNotProd2");
            System.out.println("NotProd.initNotProd3");

            Article articleFirst = Article.builder()
                    .title("제목")
                    .body("내용")
                    .build();

            Article articleSecond = Article.builder()
                    .title("제목")
                    .body("내용")
                    .build();

            System.out.println("articleFirst.id : " + articleFirst.getId());
            System.out.println("articleSecond.id : " + articleSecond.getId());

            articleRepository.save(articleFirst);
            articleRepository.save(articleSecond);

            System.out.println("articleFirst.id : " + articleFirst.getId());
            System.out.println("articleSecond.id : " + articleSecond.getId());
        };
    }
}