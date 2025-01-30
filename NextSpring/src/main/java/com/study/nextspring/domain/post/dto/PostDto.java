package com.study.nextspring.domain.post.dto;

import com.study.nextspring.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostDto {
    private long id;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private long authorId;

    private String authorName;

    private String title;

    private boolean published;

    private boolean listed;

    public PostDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.modifyDate = post.getModifyDate();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getName();
        this.title = post.getTitle();
        this.published = post.isPublished();
        this.listed = post.isListed();
    }
}
