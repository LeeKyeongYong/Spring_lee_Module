package com.example.sb_search.domain.post.postDocument.document;

import com.example.sb_search.domain.post.dto.PostDto;
import com.example.sb_search.global.standard.UtBase;
import com.meilisearch.sdk.model.Results;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDocument {
    @NonNull
    private long id;
    @NonNull
    private LocalDateTime createDate;
    @NonNull
    private long createTimeStamp;
    @NonNull
    private LocalDateTime modifyDate;
    @NonNull
    private long modifyTimeStamp;
    @NonNull
    private String subject;
    @NonNull
    private String body;

    public PostDocument(PostDto postDto) {
        this.id = postDto.getId();
        this.createDate = postDto.getCreateDate();
        this.createTimeStamp = UtBase.time.toTimeStamp(postDto.getCreateDate());
        this.modifyDate = postDto.getModifyDate();
        this.modifyTimeStamp = UtBase.time.toTimeStamp(postDto.getModifyDate());
        this.subject = postDto.getSubject();
        this.body = postDto.getBody();
    }
}