package com.study.mstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MstudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MstudyApplication.class, args);
    }

}
