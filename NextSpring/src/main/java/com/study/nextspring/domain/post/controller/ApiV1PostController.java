package com.study.nextspring.domain.post.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.post.dto.PostDto;
import com.study.nextspring.domain.post.dto.PostWithContentDto;
import com.study.nextspring.domain.post.dto.res.PostStatisticsResBody;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.base.KwTypeV1;
import com.study.nextspring.global.exception.ServiceException;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import com.study.nextspring.global.pagination.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.study.nextspring.domain.post.dto.req.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "ApiV1PostController", description = "API 글 컨트롤러")
public class ApiV1PostController {
    private final PostService postService;
    private final ReqData rq;

    @GetMapping("/mine")
    @Transactional(readOnly = true)
    @Operation(summary = "내글 다건 조회")
    public PageDto<PostDto> mine(
            @RequestParam(name = "searchKeywordType",defaultValue = "title") KwTypeV1 searchKeywordType,
            @RequestParam(name = "searchKeyword",defaultValue = "") String searchKeyword,
            @RequestParam(name = "page",defaultValue = "1") int page,
            @RequestParam(name = "pageSize",defaultValue = "10") int pageSize
    ) {
        Member actor = rq.getActor();

        return new PageDto<>(
                postService.findByAuthorPaged(actor, searchKeywordType, searchKeyword, page, pageSize)
                        .map(PostDto::new)
        );
    }

    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "공개글 다건 조회")
    public PageDto<PostDto> items(
            @RequestParam(name = "searchKeywordType",defaultValue = "title") KwTypeV1 searchKeywordType,
            @RequestParam(name = "searchKeyword",defaultValue = "") String searchKeyword,
            @RequestParam(name = "page",defaultValue = "1") int page,
            @RequestParam(name = "pageSize",defaultValue = "10") int pageSize
    ) {
        return new PageDto<>(
                postService.findByListedPaged(true, searchKeywordType, searchKeyword, page, pageSize)
                        .map(PostDto::new)
        );
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "단건 조회", description = "비밀글은 작성자만 조회 가능")
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
    @Operation(summary = "글 작성")
    public RespData<PostWithContentDto> write(
            @RequestBody @Valid PostWriteReqBody reqBody
    ) {
        Member actor = rq.findByActor().get();

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
    @Operation(summary = "글 수정")
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
    @Operation(summary = "글 삭제", description = "작성자 본인 뿐 아니라 관리자도 삭제 가능")
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
    @Operation(summary = "통계정보")
    public PostStatisticsResBody statistics() {
        Member actor = rq.getActor();

        return new PostStatisticsResBody(
                10,
                10,
                10);
    }
}