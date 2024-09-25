package com.jstudy.hibernate2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.jstudy.hibernate2")
public class Hibernate2Application {

    public static void main(String[] args) {
        SpringApplication.run(Hibernate2Application.class, args);
    }

}
