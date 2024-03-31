package com.example.sb_search.domain.post.repository;

import com.example.sb_search.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOrderByIdDesc();
}