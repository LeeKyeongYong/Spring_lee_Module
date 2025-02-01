package com.study.nextspring.global.security;

import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.base.UtClass;
import com.study.nextspring.global.httpsdata.RespData;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                // GET 요청에 대한 권한 설정
                                .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments")
                                .permitAll()
                                // 로그인, 회원가입 관련 권한 설정
                                .requestMatchers("/api/*/members/login", "/api/*/members/logout", "/api/*/members/join")
                                .permitAll()
                                // H2 콘솔 접근 허용
                                .requestMatchers("/h2-console/**")
                                .permitAll()
                                // 그 외 모든 요청은 인증 필요
                                .requestMatchers("/api/*/posts/statistics")
                                .hasAuthority("ADMIN")
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
                .sessionManagement(
                        sessionManagement -> sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setContentType("application/json;charset=UTF-8");

                                            response.setStatus(401);
                                            response.getWriter().write(
                                                    UtClass.json.toString(
                                                            new RespData("403-1", "권한이 없습니다.")
                                                    )
                                            );
                                        }
                                )
                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            response.setContentType("application/json;charset=UTF-8");


                                            response.setStatus(403);
                                            response.getWriter().write(
                                                    UtClass.json.toString(
                                                            new RespData("403-1", "접근 권한이 없습니다.")
                                                    )
                                            );
                                        }
                                )
                )
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