package com.fly.clstudy.global.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.fly.clstudy.global.security.CustomAuthenticationFilter;
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                .requestMatchers("/actuator/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .headers(
                        headers ->
                                headers.frameOptions(
                                        frameOptions ->
                                                frameOptions.sameOrigin()
                                )
                )
                .csrf(
                        csrf ->
                                csrf.disable()
                )
                .formLogin(
                        formLogin ->
                                formLogin
                                        .permitAll()
                )
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(
                        logout ->
                                logout
                                        .logoutRequestMatcher(
                                                new AntPathRequestMatcher("/logout")
                                        )
                );

        return http.build();
    }
}
