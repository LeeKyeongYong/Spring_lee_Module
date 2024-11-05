package com.study.nextspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NextSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(NextSpringApplication.class, args);
    }

}
