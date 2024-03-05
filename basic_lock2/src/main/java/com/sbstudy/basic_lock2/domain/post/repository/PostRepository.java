package com.sbstudy.basic_lock2.domain.post.repository;

import com.sbstudy.basic_lock2.domain.post.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Post> findWithShareLockById(long id);
}
