package com.sbstudy.basic_lock2.domain.post.repository;

import com.sbstudy.basic_lock2.domain.post.entity.Post;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    
    //비관적인 락
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Post> findWithShareLockById(long id);

    //쓰기락
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Post> findWithWriteLockById(long id);
}
