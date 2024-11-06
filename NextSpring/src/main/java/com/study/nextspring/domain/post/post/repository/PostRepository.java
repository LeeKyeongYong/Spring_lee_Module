package com.study.nextspring.domain.post.post.repository;

import com.study.nextspring.domain.post.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    List<Post> findByOrderByIdDesc();
}
