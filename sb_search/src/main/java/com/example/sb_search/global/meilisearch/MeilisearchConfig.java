package com.example.sb_search.global.meilisearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.json.JacksonJsonHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class MeilisearchConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public Client meilisearchClient() {
        return new Client(
                new Config(
                        "http://localhost:7700",
                        "WCUS0XL8RarcoqoVHh4-J9ywmsKNmUM-zWwGDlXcHz0",
                        //"masterKey",
                        new JacksonJsonHandler(objectMapper)
                )
        );
    }
}