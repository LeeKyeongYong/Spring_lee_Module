package com.study.nextspring.domain.post.repository;

import com.study.nextspring.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>, PostRepositoryCustom {
    List<Post> findByOrderByIdDesc();
}
