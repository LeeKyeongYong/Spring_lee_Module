package com.example.sb_search.domain.post.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/ap1/v1/posts")
@RequiredArgsConstructor
public class ApiV1PostController {
    private final PostService postService;
}