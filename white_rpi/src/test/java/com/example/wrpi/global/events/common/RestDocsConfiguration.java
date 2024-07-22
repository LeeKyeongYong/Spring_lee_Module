package com.example.wrpi.global.events.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration // test에서만 사용하는 configuration
public class RestDocsConfiguration {
    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer () {
        return configurer -> {
            configurer.operationPreprocessors()
                    .withRequestDefaults(prettyPrint()) // 요청 본문 예쁘게 출력
                    .withResponseDefaults(prettyPrint()); // 응답 본문 예쁘게 출력
        };
    }
}