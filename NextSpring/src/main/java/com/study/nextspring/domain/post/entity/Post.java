package com.study.nextspring.domain.post.entity;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.global.base.Empty;
import com.study.nextspring.global.exception.ServiceException;
import com.study.nextspring.global.httpsdata.RespData;
import com.study.nextspring.global.jpa.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTime {
    @ManyToOne(fetch = FetchType.LAZY)
    private Member author;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<PostComment> comments = new ArrayList<>();

    private boolean published;

    private boolean listed;

    public PostComment addComment(Member author, String content) {
        PostComment comment = PostComment.builder()
                .post(this)
                .author(author)
                .content(content)
                .build();

        comments.add(comment);

        return comment;
    }

    public List<PostComment> getCommentsByOrderByIdDesc() {
        List<PostComment> reversedComments = new ArrayList<>(comments);
        Collections.reverse(reversedComments);
        return reversedComments;
    }

    public Optional<PostComment> getCommentById(long commentId) {
        return comments.stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst();
    }

    public void removeComment(PostComment postComment) {
        comments.remove(postComment);
    }

    public RespData<Empty> getCheckActorCanDeleteRs(Member actor) {
        if (actor == null) return new RespData<>("401-1", "로그인 후 이용해주세요.");

        if (actor.isAdmin()) return RespData.OK;

        if (actor.equals(author)) return RespData.OK;

        return new RespData<>("403-1", "작성자만 글을 삭제할 수 있습니다.");
    }


    public void checkActorCanDelete(Member actor) {
        Optional.of(getCheckActorCanDeleteRs(actor))
                .filter(RespData::isFail)
                .ifPresent(rsData -> {
                    throw new ServiceException(rsData.getResultCode(), rsData.getMsg());
                });
    }

    public RespData<Empty> getCheckActorCanModifyRs(Member actor) {
        if (actor == null) return new RespData<>("401-1", "로그인 후 이용해주세요.");

        if (actor.equals(author)) return RespData.OK;

        return new RespData<>("403-1", "작성자만 글을 수정할 수 있습니다.");
    }

    public void checkActorCanModify(Member actor) {
        if (actor == null) {
            throw new ServiceException("401-1", "로그인 후 이용해주세요.");
        }
        if (actor.equals(author)) return;

        throw new ServiceException("403-1", "작성자만 글을 수정할 수 있습니다.");
    }

    public void checkActorCanRead(Member actor) {
        Optional.of(getCheckActorCanModifyRs(actor))
                .filter(RespData::isFail)
                .ifPresent(rsData -> {
                    throw new ServiceException(rsData.getResultCode(), rsData.getMsg());
                });
    }

}