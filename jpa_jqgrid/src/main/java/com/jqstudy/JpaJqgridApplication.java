package com.jqstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.jqstudy.domain.entity")
public class JpaJqgridApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaJqgridApplication.class, args);
    }

}
