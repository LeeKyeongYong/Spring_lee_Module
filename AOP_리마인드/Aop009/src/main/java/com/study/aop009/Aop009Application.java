package com.study.aop009;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.study.aop009")
public class Aop009Application {

    public static void main(String[] args) {
        SpringApplication.run(Aop009Application.class, args);
    }

}
