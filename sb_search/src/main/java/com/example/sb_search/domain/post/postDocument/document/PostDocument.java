package com.example.sb_search.domain.post.postDocument.document;

import com.example.sb_search.domain.post.dto.PostDto;
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