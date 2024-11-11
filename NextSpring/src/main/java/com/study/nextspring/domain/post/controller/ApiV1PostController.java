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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final ReqData rq;

    @GetMapping
    public ResponseEntity<Page<PostDto>> getPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "${app.config.basePageSize:20}") int size,
            @RequestParam(name = "kw", defaultValue = "") String kw,
            @RequestParam(name = "kwType", defaultValue = "ALL") KwTypeV1 kwType,
            @RequestParam(name = "published", defaultValue = "false") Boolean published,
            @RequestParam(name = "listed", defaultValue = "false") Boolean listed
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<Post> postPage = postService.findByKw(kwType, kw, null, published, listed, pageable);
        Page<PostDto> postDtoPage = postPage.map(PostDto::new);
        return new ResponseEntity<>(postDtoPage, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<Page<PostDto>> getPosts(
//            @RequestParam(name = "page", defaultValue = "0") int page,
//            @RequestParam(name = "size", defaultValue = "10") int size,
//            @RequestParam(name = "kw", defaultValue = "") String kw,
//            @RequestParam(name = "kwType", defaultValue = "ALL") KwTypeV1 kwType,
//            @RequestParam(name = "published", defaultValue = "false") Boolean published,
//            @RequestParam(name = "listed", defaultValue = "false") Boolean listed
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
//        Page<Post> postPage = postService.findByKw(kwType, kw, null, published, listed, pageable);
//        Page<PostDto> postDtoPage = postPage.map(PostDto::new);
//        return new ResponseEntity<>(postDtoPage, HttpStatus.OK);
//    }

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
    public ResponseEntity<Post> writeItem(@RequestBody @Valid PostWriteItemReqBody reqBody, @AuthenticationPrincipal Member author) {
        // 로그인된 사용자가 없다면 에러 반환
        if (author == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // 새 게시글 작성
        Post post = postService.write(author, reqBody.getTitle(), reqBody.getBody(), reqBody.isPublished(), reqBody.isListed());

        // 작성한 게시글 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
}