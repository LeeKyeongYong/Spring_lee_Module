package com.example.sb_search.domain.post.event;

import com.example.sb_search.domain.post.dto.PostDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class AfterPostModifiedEvent extends ApplicationEvent {
    @Getter
    private final PostDto postDto;

    public AfterPostModifiedEvent(Object source, PostDto postDto) {
        super(source);
        this.postDto = postDto;
    }
}