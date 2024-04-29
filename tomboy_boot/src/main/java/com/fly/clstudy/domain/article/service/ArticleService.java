package com.fly.clstudy.domain.article.service;

import com.fly.clstudy.domain.article.entity.Article;
import com.fly.clstudy.domain.article.repository.ArticleRepository;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;

    public long count() {
        return articleRepository.count();
    }

    @Transactional
    public RespData<Article> write(Member author, String title, String body) {
        Article article = Article
                .builder()
                .title(title)
                .body(body)
                .author(author)
                .build();

        articleRepository.save(article);

        return RespData.of("%d번 게시물이 작성되었습니다.".formatted(article.getId()), article);
    }

    @Transactional
    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public Optional<Article> findById(long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
