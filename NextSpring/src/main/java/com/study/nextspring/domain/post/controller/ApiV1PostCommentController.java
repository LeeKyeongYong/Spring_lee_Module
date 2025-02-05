package com.study.nextspring.domain.post.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.post.dto.PostCommentDto;
import com.study.nextspring.domain.post.dto.req.PostCommentModifyReqBody;
import com.study.nextspring.domain.post.dto.req.PostCommentWriteReqBody;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.entity.PostComment;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "ApiV1PostCommentController", description = "API 댓글 컨트롤러")
public class ApiV1PostCommentController {
    private final PostService postService;
    private final ReqData rq;

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "다건조회")
    public List<PostCommentDto> items(
            @PathVariable long postId
    ) {
        Post post = postService.findById(postId).orElseThrow(
                () -> new ServiceException("%d번 글은 존재하지 않습니다.".formatted(postId))
        );

        return post
                .getComments()
                .stream()
                .map(PostCommentDto::new)
                .toList();
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    public RespData<Void> delete(
            @PathVariable long postId,
            @PathVariable long id
    ) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId).orElseThrow(
                () -> new ServiceException("%d번 글은 존재하지 않습니다.".formatted(postId))
        );

        PostComment postComment = post.getCommentById(id).orElseThrow(
                () -> new ServiceException("%d번 댓글은 존재하지 않습니다.".formatted(id))
        );

        postComment.checkActorCanDelete(actor);

        post.removeComment(postComment);

        return new RespData<>(
                "200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(id)
        );
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RespData<PostCommentDto> modify(
            @PathVariable long postId,
            @PathVariable long id,
            @RequestBody @Valid PostCommentModifyReqBody reqBody
    ) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId).orElseThrow(
                () -> new ServiceException("%d번 글은 존재하지 않습니다.".formatted(postId))
        );

        PostComment postComment = post.getCommentById(id).orElseThrow(
                () -> new ServiceException("%d번 댓글은 존재하지 않습니다.".formatted(id))
        );

        postComment.checkActorCanModify(actor);

        postComment.modify(reqBody.content());

        return RespData.of("200-1", "%d번 댓글이 수정되었습니다.".formatted(id), new PostCommentDto(postComment));
    }


    @PostMapping
    @Transactional
    @Operation(summary = "작성")
    public RespData<PostCommentDto> write(
            @PathVariable long postId,
            @RequestBody @Valid PostCommentWriteReqBody reqBody
    ) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId).orElseThrow(
                () -> new ServiceException("%d번 글은 존재하지 않습니다.".formatted(postId))
        );

        PostComment postComment = post.addComment(
                actor,
                reqBody.content()
        );

        postService.flush();


        return RespData.<PostCommentDto>of("200-1", "%d번 댓글이 수정되었습니다.".formatted(postComment.getId()), new PostCommentDto(postComment));

    }
}