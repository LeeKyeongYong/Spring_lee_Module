package com.study.nextspring.domain.post.dto;

import com.study.nextspring.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
public class PostDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String authorProfileImgUrl;
    private String title;
    private String body;
    private boolean published;
    private boolean listed;

    @Setter
    private Boolean actorCanRead;
    @Setter
    private Boolean actorCanModify;
    @Setter
    private Boolean actorCanDelete;

    public PostDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.modifyDate = post.getModifyDate();
        this.authorId = post.getAuthor().getId();
        this.authorName = post.getAuthor().getName();
        this.authorProfileImgUrl = post.getAuthor().getProfileImgUrlOrDefault();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.published = post.isPublished();
        this.listed = post.isListed();
    }
}
