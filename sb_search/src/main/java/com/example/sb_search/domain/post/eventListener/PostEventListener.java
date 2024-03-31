package com.example.sb_search.domain.post.eventListener;

import com.example.sb_search.domain.post.event.AfterPostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostEventListener {
    @EventListener
    @Async
    public void listen(AfterPostCreatedEvent event) {
        System.out.println("event = " + event.getPost());
    }
}