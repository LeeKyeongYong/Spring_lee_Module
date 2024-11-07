package com.study.nextspring.domain.post.service;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.repository.PostRepository;
import com.study.nextspring.global.base.KwTypeV1;
import com.study.nextspring.global.exception.GlobalException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Post write(Member author, String title, String body, boolean published, boolean listed) {
        Post post = Post.builder()
                .author(author)
                .title(title)
                .body(body)
                .published(published)
                .listed(listed)
                .build();

        postRepository.save(post);

        return post;
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

    @Transactional
    public void modify(Post post, @NotBlank String title,@NotBlank String body){
        post.setTitle(title);
        post.setBody(body);
    }

    public Page<Post> findByKw(KwTypeV1 kwType, String kw, Member author, Boolean published, Boolean listed, Pageable pageable) {
        return postRepository.findByKw(kwType, kw, author, published, listed, pageable);
    }

    public void checkCanRead(Member actor, Post post) {
        if (!canRead(actor, post)) {
            throw new GlobalException("403-1", "권한이 없습니다.");
        }
    }

    public boolean canRead(Member actor, Post post) {
        if (post == null) return false;
        if (actor == null) return post.isPublished();

        return actor.equals(post.getAuthor()) || post.isPublished();
    }

    public void checkCanDelete(Member actor, Post post) {
        if (!canDelete(actor, post)) throw new GlobalException("403-1", "권한이 없습니다.");
    }

    public boolean canDelete(Member actor, Post post) {

        if (post == null) return false;
        if (actor == null) return post.isPublished();

        return actor.equals(post.getAuthor());
    }

    public void checkCanModify(Member actor, Post post) {
        if (!canModify(actor, post)) throw new GlobalException("403-1", "권한이 없습니다.");
    }

    public boolean canModify(Member actor, Post post) {

        if (post == null) return false;
        if (actor == null) return post.isPublished();

        return actor.equals(post.getAuthor());
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }
}
