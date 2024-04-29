package com.fly.clstudy.article.dto;

import com.fly.clstudy.article.entity.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static lombok.AccessLevel.PROTECTED;
@NoArgsConstructor(access = PROTECTED)
@Getter
public class ArticleDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String title;
    private String body;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.createDate = article.getCreateDate();
        this.modifyDate = article.getModifyDate();
        this.authorId = article.getAuthor().getId();
        this.authorName = article.getAuthor().getUsername();
        this.title = article.getTitle();
        this.body = article.getBody();
    }
}
