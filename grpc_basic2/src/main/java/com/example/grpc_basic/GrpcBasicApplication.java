package com.example.grpc_basic;

import net.devh.boot.grpc.server.autoconfigure.GrpcHealthServiceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {GrpcHealthServiceAutoConfiguration.class})
public class GrpcBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrpcBasicApplication.class, args);
    }

}
