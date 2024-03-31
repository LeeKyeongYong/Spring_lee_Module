package com.example.sb_search.domain.post.service;

import com.example.sb_search.domain.post.dto.PostDto;
import com.example.sb_search.domain.post.entity.Post;
import com.example.sb_search.domain.post.event.AfterPostCreatedEvent;
import com.example.sb_search.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Post write(String subject, String body) {
        Post post = postRepository.save(
                Post.builder()
                        .subject(subject)
                        .body(body)
                        .build());

        publisher.publishEvent(new AfterPostCreatedEvent(this, new PostDto(post)));

        return post;
    }

    public long count() {
        return postRepository.count();
    }
}