package com.example.sb_search.domain.post.postDocument.document;

import com.example.sb_search.domain.post.dto.PostDto;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
public class PostDocument {
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

    public PostDocument(PostDto postDto) {
        this.id = postDto.getId();
        this.createDate = postDto.getCreateDate();
        this.modifyDate = postDto.getModifyDate();
        this.subject = postDto.getSubject();
        this.body = postDto.getBody();
    }
}