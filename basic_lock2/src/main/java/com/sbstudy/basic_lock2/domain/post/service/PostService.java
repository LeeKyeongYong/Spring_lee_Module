package com.sbstudy.basic_lock2.domain.post.service;

import com.sbstudy.basic_lock2.domain.post.entity.Post;
import com.sbstudy.basic_lock2.domain.post.repository.PostRepository;
import com.sbstudy.basic_lock2.global.jpa.rsdata.RespData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public RespData<Post> write(String title) {
        Post post = postRepository.save(
                Post.builder()
                        .title(title)
                        .build()
        );

        return RespData.of(post);
    }

    public long count() {
        return postRepository.count();
    }
}