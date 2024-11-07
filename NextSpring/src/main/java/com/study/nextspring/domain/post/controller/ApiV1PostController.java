package com.study.nextspring.domain.post.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.domain.post.dto.PostModifyItemReqBody;
import com.study.nextspring.domain.post.dto.PostWriteItemReqBody;
import com.study.nextspring.domain.post.service.PostService;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.base.KwTypeV1;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping
    public Page<Post> getItems(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "ALL") KwTypeV1 kwType
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Post> itemPage = postService.findByKw(kwType, kw, null, true, true, pageable);

        return itemPage;
    }

    @GetMapping("/{id}")
    public Post getItem(@PathVariable long id) {
        return postService.findById(id).get();
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable long id){
        postService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Post modifyItem(@PathVariable long id, @RequestBody @Valid PostModifyItemReqBody reqBody){
        Post post = postService.findById(id).get();
        postService.modify(post,reqBody.title,reqBody.body);
        return post;
    }

    @PostMapping
    public Post writeItem( @RequestBody @Valid PostWriteItemReqBody reqBody) {
        Member author = memberService.findById(3).get();
        return postService.write(author, reqBody.title, reqBody.body, reqBody.isPublished(), reqBody.isListed());
    }
}
