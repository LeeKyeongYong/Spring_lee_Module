package com.example.module_app_member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ModuleAppMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModuleAppMemberApplication.class, args);
	}

}
