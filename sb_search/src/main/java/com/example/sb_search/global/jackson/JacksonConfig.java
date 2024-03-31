package com.example.sb_search.global.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        // ObjectMapper의 인스턴스를 생성하고 커스터마이징합니다.
        ObjectMapper objectMapper = builder.build();

        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }
}