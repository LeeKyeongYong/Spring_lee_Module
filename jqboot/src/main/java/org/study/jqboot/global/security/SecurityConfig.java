package org.study.jqboot.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SecurityConfig {

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                )
                .csrf().disable();

        return httpSecurity.build();
    }*/
}