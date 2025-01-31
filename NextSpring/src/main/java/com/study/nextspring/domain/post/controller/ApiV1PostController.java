package com.study.nextspring.domain.post.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.post.dto.PostDto;
import com.study.nextspring.domain.post.dto.PostWithContentDto;
import com.study.nextspring.domain.post.dto.res.PostStatisticsResBody;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.exception.ServiceException;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import com.study.nextspring.global.pagination.PageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.study.nextspring.domain.post.dto.req.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
    private final ReqData rq;

    @GetMapping("/mine")
    @Transactional(readOnly = true)
    public PageDto<PostDto> mine(
            @RequestParam(defaultValue = "title") String searchKeywordType,
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Member actor = rq.getActor();

        return new PageDto<>(
                postService.findByAuthorPaged(actor, searchKeywordType, searchKeyword, page, pageSize)
                        .map(PostDto::new)
        );
    }

    @GetMapping
    @Transactional(readOnly = true)
    public PageDto<PostDto> items(
            @RequestParam(defaultValue = "title") String searchKeywordType,
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return new PageDto<>(
                postService.findByListedPaged(true, searchKeywordType, searchKeyword, page, pageSize)
                        .map(PostDto::new)
        );
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostWithContentDto item(@PathVariable long id) {
        Post post = postService.findById(id).get();

        if (!post.isPublished()) {
            Member actor = rq.getActor();

            if (actor == null) {
                throw new ServiceException("401-1", "로그인이 필요합니다.");
            }

            post.checkActorCanRead(actor);
        }

        return new PostWithContentDto(post);
    }


    @PostMapping
    @Transactional
    public RespData<PostWithContentDto> write(
            @RequestBody @Valid PostWriteReqBody reqBody
    ) {
        Member actor = rq.getActor();

        Post post = postService.write(
                actor,
                reqBody.title(),
                reqBody.content(),
                reqBody.published(),
                reqBody.listed()
        );

        return RespData.of("201-1", "%d번 글이 작성되었습니다.".formatted(post.getId()), new PostWithContentDto(post));
    }


    @PutMapping("/{id}")
    @Transactional
    public RespData<PostWithContentDto> modify(
            @PathVariable long id,
            @RequestBody @Valid PostModifyReqBody reqBody
    ) {
        Member actor = rq.getActor();

        Post post = postService.findById(id).get();

        post.checkActorCanModify(actor);

        postService.modify(post, reqBody.title(), reqBody.content(), reqBody.published(), reqBody.listed());

        postService.flush();

        return RespData.of("200-1", "%d번 글이 수정되었습니다.".formatted(id), new PostWithContentDto(post));
    }


    @DeleteMapping("/{id}")
    @Transactional
    public RespData<Void> delete(
            @PathVariable long id
    ) {
        Member member = rq.getActor();

        Post post = postService.findById(id).get();

        post.checkActorCanDelete(member);

        postService.delete(post);

        return new RespData<>("200-1", "%d번 글이 삭제되었습니다.".formatted(id));
    }

    @GetMapping("/statistics")
    @Transactional(readOnly = true)
    public PostStatisticsResBody statistics() {
        Member actor = rq.getActor();

        //if (!actor.isAdmin()) throw new ServiceException("403-1", "관리자만 접근 가능합니다.");

        return new PostStatisticsResBody(
                10,
                10,
                10);
    }
}