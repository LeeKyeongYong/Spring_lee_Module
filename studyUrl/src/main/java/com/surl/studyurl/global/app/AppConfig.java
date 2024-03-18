package com.surl.studyurl.global.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    public static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

}
