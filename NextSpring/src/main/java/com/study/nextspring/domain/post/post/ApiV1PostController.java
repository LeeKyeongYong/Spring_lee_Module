package com.study.nextspring.domain.post.post;

import com.study.nextspring.domain.post.post.dto.PostModifyItemReqBody;
import com.study.nextspring.domain.post.post.entity.Post;
import com.study.nextspring.domain.post.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;

    @GetMapping
    public List<Post> getItems() {
        return postService.findByOrderByIdDesc();
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
}
