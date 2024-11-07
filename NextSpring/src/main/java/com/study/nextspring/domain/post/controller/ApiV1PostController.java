package com.study.nextspring.domain.post.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.domain.post.dto.PostDto;
import com.study.nextspring.domain.post.dto.PostModifyItemReqBody;
import com.study.nextspring.domain.post.dto.PostWriteItemReqBody;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.base.KwTypeV1;
import com.study.nextspring.global.httpsdata.ReqData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final ReqData rq;

    @GetMapping
    public Page<PostDto> getItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeV1 kwType,
            Authentication authentication) {
        try {
            Member actor = (authentication != null && authentication.getPrincipal() instanceof Member)
                    ? (Member) authentication.getPrincipal()
                    : null;
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
            Page<Post> itemPage = postService.findByKw(kwType, kw, actor, true, true, pageable);
            Page<PostDto> postDtos = itemPage.map(post -> toPostDto(actor, post));
            return postDtos;
        } catch (Exception e) {
            // 적절한 예외 처리 로직 추가
            throw new RuntimeException("Failed to fetch posts", e);
        }
    }

    @GetMapping("/{id}")
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
    public void deleteItem(
            @PathVariable long id
    ) {
        Member actor = rq.getMember();

        Post post = postService.findById(id).get();

        postService.checkCanDelete(actor, post);

        postService.delete(post);
    }



    @PutMapping("/{id}")
    public Post modifyItem(
            @PathVariable long id,
            @RequestBody @Valid PostModifyItemReqBody reqBody
    ) {
        Member actor = rq.getMember();

        Post post = postService.findById(id).get();

        postService.checkCanModify(actor, post);

        postService.modify(post, reqBody.title, reqBody.body);

        return post;
    }

    @PostMapping
    public Post writeItem(
            @RequestBody @Valid PostWriteItemReqBody reqBody
    ) {
        Member author = memberService.findById(3).get();

        return postService.write(author, reqBody.title, reqBody.body, reqBody.isPublished(), reqBody.isListed());
    }
}