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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;

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

    public Optional<Post> findById(long id){
        return postRepository.findById(id);
    }

    public Optional<Post> findWithShareLockById(long id) {
        return postRepository.findWithShareLockById(id);
    }

    public Optional<Post> findWithWriteLockById(long id) {
        return postRepository.findWithWriteLockById(id);
    }

    @Transactional
    public Post modifyWithPessimistic(long id, String title) {
        Post post = postRepository.findWithWriteLockById(id).get();
        post.setTitle(title);

        return post;
    }

    @Transactional
    public Post modifyWithOptimistic(long id, String title) {
        Post post = postRepository.findById(id).get();
        post.setTitle(title);

        return post;
    }
}