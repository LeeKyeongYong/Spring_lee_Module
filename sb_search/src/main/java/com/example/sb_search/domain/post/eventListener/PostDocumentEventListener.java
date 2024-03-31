package com.example.sb_search.domain.post.eventListener;

import com.example.sb_search.domain.post.dto.PostDto;
import com.example.sb_search.domain.post.event.AfterPostCreatedEvent;
import com.example.sb_search.domain.post.postDocument.service.PostDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDocumentEventListener {

    private final PostDocumentService postDocumentService;


    @EventListener
    @Async
    public void listen(AfterPostCreatedEvent event) {
        PostDto postDto = event.getPostDto();

        postDocumentService.add(postDto);
    }
}