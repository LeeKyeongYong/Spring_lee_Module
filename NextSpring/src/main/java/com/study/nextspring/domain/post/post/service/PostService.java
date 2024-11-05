package com.study.nextspring.domain.post.post.service;

import com.study.nextspring.domain.post.post.entity.Post;
import com.study.nextspring.domain.post.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public void write(String title, String body) {
        Post post = Post.builder()
                .title(title)
                .body(body)
                .build();

        postRepository.save(post);
    }

    public long count() {
        return postRepository.count();
    }

    public List<Post> findByOrderByIdDesc() {
        return postRepository.findByOrderByIdDesc();
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    @Transactional
    public void deleteById(long id){
        postRepository.deleteById(id);
    }
}
