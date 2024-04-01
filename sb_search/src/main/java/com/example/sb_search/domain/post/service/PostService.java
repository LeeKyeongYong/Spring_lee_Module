package com.example.sb_search.domain.post.service;

import com.example.sb_search.domain.post.dto.PostDto;
import com.example.sb_search.domain.post.entity.Post;
import com.example.sb_search.domain.post.event.AfterPostCreatedEvent;
import com.example.sb_search.domain.post.event.AfterPostModifiedEvent;
import com.example.sb_search.domain.post.postDocument.document.PostDocument;
import com.example.sb_search.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    // private final ApplicationEventPublisher publisher;
    private final KafkaTemplate<Object, Object> template;

    @Transactional
    public Post write(String subject, String body) {
        Post post = postRepository.save(
                Post.builder()
                        .subject(subject)
                        .body(body)
                        .build());

        template.send("AfterPostCreatedEvent", new PostDto(post));

        return post;
    }

    public long count() {
        return postRepository.count();
    }

    public List<Post> findAll() {
        return postRepository.findByOrderByIdDesc();
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public void modified(Post post) {
        // publisher.publishEvent(new AfterPostModifiedEvent(this, new PostDto(post)));
        template.send("AfterPostModifiedEvent", new PostDto(post));
    }
}