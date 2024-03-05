package com.sbstudy.basic_lock2.domain.post.controller;

import com.sbstudy.basic_lock2.domain.post.entity.Post;
import com.sbstudy.basic_lock2.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostController {

    private final PostService postService;

    @GetMapping("/{id}")
    public Post getPost(@PathVariable long id){
        return postService.findById(id).get();
    }

}
