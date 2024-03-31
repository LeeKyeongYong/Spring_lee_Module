package com.example.sb_search.global.meilisearch;

import com.meilisearch.sdk.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeilisearchConfig {
    @Bean
    public Client meilisearchClient() {
        return new Client(new Config("http://localhost:7700", "masterKey"));
    }
}