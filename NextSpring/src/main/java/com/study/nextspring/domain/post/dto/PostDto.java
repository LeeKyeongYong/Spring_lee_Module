package com.study.nextspring.domain.post.dto;

import com.study.nextspring.domain.post.entity.Post;
import lombok.Getter;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;

@Getter
public class PostDto {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private long authorId;
    @NonNull
    private String authorName;
    @NonNull
    private String title;
    @NonNull
    private boolean published;
    @NonNull
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
