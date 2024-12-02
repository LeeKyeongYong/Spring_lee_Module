package com.study.nextspring.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.base.UtClass;
import com.study.nextspring.global.httpsdata.RespData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts")
                                .permitAll()
                                .requestMatchers("/api/*/members/login", "/api/*/members/logout")
                                .permitAll()
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                .requestMatchers("/actuator/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/")
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
                ).sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                ).exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");
                                            response.setStatus(403);
                                            response.setHeader("Access-Control-Allow-Credentials", "true"); // 쿠키 허용
                                            response.setHeader("Access-Control-Allow-Origin", AppConfig.getSiteFrontUrl()); // 특정 출처 허용
                                            response.getWriter().write(
                                                    UtClass.json.toString(
                                                            RespData.of("403-1", request.getRequestURI() + ", " + authException.getLocalizedMessage())
                                                    )
                                            );
                                        }
                                )
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(
                        cors ->
                                cors.configurationSource(
                                        request -> {
                                            var corsConfig = new CorsConfiguration();
                                            corsConfig.setAllowCredentials(true);
                                            corsConfig.addAllowedOrigin(AppConfig.getSiteFrontUrl());
                                            corsConfig.addAllowedHeader("*");
                                            corsConfig.addAllowedMethod("*");

                                            return corsConfig;
                                        }
                                )
                );

        return http.build();
    }
}
