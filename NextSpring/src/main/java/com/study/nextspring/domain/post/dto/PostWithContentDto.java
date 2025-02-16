package com.study.nextspring.domain.post.dto;

import com.study.nextspring.domain.post.entity.Post;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;
import java.time.LocalDateTime;

@Getter
public class PostWithContentDto extends PostDto {

    @NonNull
    private String content;
    @NonNull
    private boolean published;
    @NonNull
    private boolean listed;

    @Setter
    private Boolean actorCanModify;

    @Setter
    private Boolean actorCanDelete;

    public PostWithContentDto(Post post) {
        super(post);
        this.content = post.getContent();
        this.published = post.isPublished();
        this.listed = post.isListed();
    }
}