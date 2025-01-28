package com.dstudy.dstudy_01.config;

import org.jooq.conf.Settings;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

//    @Bean
//    Settings jooqSettings() {
//        return new Settings()
//                .withRenderSchema(false);
//    }
    @Bean
    public DefaultConfigurationCustomizer jooqDefaultConfigurationCustomizer() {
        return c -> c.settings().withRenderSchema(false);
    }
}