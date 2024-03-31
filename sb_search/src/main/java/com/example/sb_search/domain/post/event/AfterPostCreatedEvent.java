package com.example.sb_search.domain.post.event;

import com.example.sb_search.domain.post.dto.PostDto;
import com.example.sb_search.domain.post.entity.Post;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
public class AfterPostCreatedEvent extends ApplicationEvent {
    @Getter
    private final PostDto postDto;

    public AfterPostCreatedEvent(Object source, PostDto postDto) {
        super(source);
        this.postDto = postDto
    }
}