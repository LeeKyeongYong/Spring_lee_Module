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
                .securityMatcher("/api/**")
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Public endpoints that don't require authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/members/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/members/join").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/members/me").authenticated()
                        // Allow public access to posts listing and viewing
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/*").permitAll()
                        // Secured endpoints that require authentication
                        .requestMatchers(HttpMethod.POST, "/api/v1/posts").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/posts/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/*").authenticated()
                        // Any other request needs authentication
                        .anyRequest().authenticated()
                )
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOrigin(AppConfig.getSiteFrontUrl());
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.addAllowedOrigin(AppConfig.getSiteFrontUrl());
                    config.addAllowedHeader("*");
                    config.addAllowedMethod("*");
                    config.setExposedHeaders(Arrays.asList("Authorization")); // Authorization 헤더 노출
                    return config;
                }))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(403);
                            response.setHeader("Access-Control-Allow-Origin", AppConfig.getSiteFrontUrl());
                            response.setHeader("Access-Control-Allow-Credentials", "true");

                            Map<String, Object> errorResponse = new HashMap<>();
                            errorResponse.put("status", "403");
                            errorResponse.put("message", "Authentication required");
                            errorResponse.put("path", request.getRequestURI());

                            ObjectMapper mapper = new ObjectMapper();
                            response.getWriter().write(mapper.writeValueAsString(errorResponse));
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}