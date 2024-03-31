package com.example.sb_search.domain.post.dto;

import com.example.sb_search.domain.post.entity.Post;
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
    private String subject;
    @NonNull
    private String body;

    public PostDto(Post post) {
        this.id = post.getId();
        this.createDate = post.getCreateDate();
        this.modifyDate = post.getModifyDate();
        this.subject = post.getSubject();
        this.body = post.getBody();
    }
}