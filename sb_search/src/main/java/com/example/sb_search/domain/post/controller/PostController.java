package com.example.sb_search.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.SearchResult;
import com.meilisearch.sdk.model.Searchable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.ArrayList;
import java.util.List;

import com.meilisearch.sdk.Config;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    //private Client client = new Client(new Config("http://localhost:7700", "WCUS0XL8RarcoqoVHh4-J9ywmsKNmUM-zWwGDlXcHz0"));

    private final Client meilisearchClient;

    @Data
    @AllArgsConstructor
    public static class Movie {
        private String id;
        private String title;
        private String[] genres;
    }

    @SneakyThrows
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


        // List를 JSON 문자열로 변환
        String documents = objectMapper.writeValueAsString(movies);

        // Meilisearch 클라이언트 설정
        //Index index = client.index("movies");

        // Meilisearch 클라이언트 설정
        Index index = meilisearchClient.index("movies");

        // 문서 추가
        index.addDocuments(documents); // => { "taskUid": 0 }

        /*
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
        */
        return "성공";
    }
    @GetMapping("/search")
    @ResponseBody
    public SearchResult search(@RequestParam("kw") String kw) {
        //Index index = client.index("movies");
        Index index = meilisearchClient.index("movies");
        SearchResult results = index.search(kw);

        return results;
    }


    @GetMapping("/customSearch")
    @ResponseBody
    public Searchable customSearch(@RequestParam("kw") String kw) {
        Index index = meilisearchClient.index("movies");
        //Index index = client.index("movies");
        SearchResult search = (SearchResult)index.search(
                new SearchRequest(kw)
                        .setShowMatchesPosition(true)
                        .setAttributesToHighlight(new String[]{"title"})
        );

        return search;
    }

    @GetMapping("/deleteIndex")
    @ResponseBody
    public String deleteIndex(@RequestParam("indexName") String indexName) {
        meilisearchClient.deleteIndex(indexName);
        //Index index = client.index(indexName);
        //String result=index.deleteDocument(indexName).toString();
        return "성공: ";
    }

    @GetMapping("/deleteAllDocuments")
    @ResponseBody
    public String deleteAllDocuments(@RequestParam("indexName") String indexName) {
        Index index = meilisearchClient.index(indexName);

        index.deleteAllDocuments();

        return "성공";
    }


}