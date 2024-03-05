package com.sbstudy.basic_lock2.domain.post.repository;

import com.sbstudy.basic_lock2.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
