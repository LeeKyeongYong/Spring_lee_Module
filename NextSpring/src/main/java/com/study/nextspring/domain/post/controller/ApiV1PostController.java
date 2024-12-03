package com.study.nextspring.domain.post.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.domain.post.dto.PostDto;
import com.study.nextspring.domain.post.dto.PostModifyItemReqBody;
import com.study.nextspring.domain.post.dto.PostWriteItemReqBody;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.base.Empty;
import com.study.nextspring.global.base.KwTypeV1;
import com.study.nextspring.global.httpsdata.ReqData;
import com.study.nextspring.global.httpsdata.RespData;
import com.study.nextspring.global.pagination.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    @Autowired private final PostService postService;
    private final MemberService memberService;
    private final ReqData rq;

    @GetMapping("/api/v1/posts")
    @Operation(summary = "다건 조회")
    public PageDto<PostDto> getItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeV1 kwType
    ) {
        // 기본값을 처리하기 위한 코드
        if (page <= 0) page = 1;
        if (kw == null || kw.isEmpty()) kw = "";
        if (kwType == null) kwType = KwTypeV1.ALL;

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Post> itemPage = postService.findByKw(kwType, kw, null, true, true, pageable);

        Member actor = rq.getMember();

        return new PageDto(
                itemPage.map(post -> toPostDto(actor, post))
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    public PostDto getItem(
            @PathVariable long id
    ) {
        Member actor = rq.getMember();

        Post post = postService.findById(id).get();

        postService.checkCanRead(actor, post);

        PostDto postDto = toPostDto(actor, post);

        return postDto;
    }

    private PostDto toPostDto(Member actor, Post post) {
        PostDto postDto = new PostDto(post);

        postDto.setActorCanRead(postService.canRead(actor, post));
        postDto.setActorCanModify(postService.canModify(actor, post));
        postDto.setActorCanDelete(postService.canDelete(actor, post));

        return postDto;
    }


    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "삭제")
    public RespData<Empty> deleteItem(
            @PathVariable long id
    ) {
        Member actor = rq.getMember();

        Post post = postService.findById(id).get();

        postService.checkCanDelete(actor, post);

        postService.delete(post);

        return RespData.of("삭제 성공");
    }



    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "수정")
    public RespData<PostDto> modifyItem(
            @PathVariable long id,
            @Valid @RequestBody PostModifyItemReqBody reqBody
    ) {
        Member actor = rq.getMember();

        Post post = postService.findById(id).get();

        postService.checkCanModify(actor, post);

        postService.modify(post, reqBody.title, reqBody.body, reqBody.isPublished(), reqBody.isListed());

        return RespData.of("%d번 글이 수정되었습니다.".formatted(id), toPostDto(actor, post));
    }

    @PostMapping
    @Transactional
    @Operation(summary = "작성")
    public RespData<PostDto> writeItem(
            @Valid @RequestBody PostWriteItemReqBody reqBody
    ) {
        Member author = rq.getMember();

        Post post = postService.write(author, reqBody.title, reqBody.body, reqBody.isPublished(), reqBody.isListed());

        return RespData.of("%d번 글이 생성되었습니다.".formatted(post.getId()), toPostDto(author, post));
    }
}