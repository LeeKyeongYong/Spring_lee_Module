package com.example.sb_search.domain.post.event;

import com.example.sb_search.domain.post.entity.Post;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
public class AfterPostCreatedEvent extends ApplicationEvent {
    @Getter
    private final Post post;

    public AfterPostCreatedEvent(Object source, Post post) {
        super(source);
        this.post = post;
    }
}