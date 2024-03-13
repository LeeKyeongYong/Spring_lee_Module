package com.surl.studyurl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyUrlApplication.class, args);
    }

}
