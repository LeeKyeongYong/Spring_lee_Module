package com.study.mbatch2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.study.mbatch2.mybatis")
public class MybatisBatch2Application {

    public static void main(String[] args) {
        SpringApplication.run(MybatisBatch2Application.class, args);
    }

}
