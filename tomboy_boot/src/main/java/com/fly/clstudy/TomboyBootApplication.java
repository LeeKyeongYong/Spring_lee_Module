package com.fly.clstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TomboyBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomboyBootApplication.class, args);
    }

}
