package com.study;

import com.study.mybatis.DummyMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.study.mybatis")
public class MybatisBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisBatchApplication.class, args);
    }
}