package com.example.sb_search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJpaAuditing
public class SbSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbSearchApplication.class, args);
    }

}
