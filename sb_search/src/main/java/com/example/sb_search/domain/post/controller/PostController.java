package com.example.sb_search.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Data
    @AllArgsConstructor
    public static class Movie {
        private String id;
        private String title;
        private String[] genres;
    }

    @GetMapping("/makeSearchData")
    @ResponseBody
    public String makeSearchData() {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Movie> movies = new ArrayList<>();

        // 영화 정보 추가
        movies.add(new Movie("1", "Carol", new String[]{"Romance", "Drama"}));
        movies.add(new Movie("2", "Wonder Woman", new String[]{"Action", "Adventure"}));
        movies.add(new Movie("3", "Life of Pi", new String[]{"Adventure", "Drama"}));
        movies.add(new Movie("4", "Mad Max: Fury Road", new String[]{"Adventure", "Science Fiction"}));
        movies.add(new Movie("5", "Moana", new String[]{"Fantasy", "Action"}));
        movies.add(new Movie("6", "Philadelphia", new String[]{"Drama"}));
        movies.add(new Movie("7", "Movie 7", new String[]{"Drama"}));

        try {
            // List를 JSON 문자열로 변환
            String documents = objectMapper.writeValueAsString(movies);

            // Meilisearch 클라이언트 설정
            Client client = new Client(new Config("http://localhost:7700", "masterKey"));
            Index index = client.index("movies");

            // 문서 추가
            index.addDocuments(documents); // => { "taskUid": 0 }
        } catch (Exception e) {
            e.printStackTrace();
            return "실패";
        }

        return "성공";
    }
}