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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // GET 요청에 대한 권한 설정
                        .requestMatchers(HttpMethod.GET, "/api/*/posts/{id:\\d+}", "/api/*/posts", "/api/*/posts/{postId:\\d+}/comments")
                        .permitAll()
                        // 로그인, 회원가입 관련 권한 설정
                        .requestMatchers("/api/*/members/login", "/api/*/members/logout", "/api/*/members/join")
                        .permitAll()
                        // H2 콘솔 접근 허용
                        .requestMatchers("/h2-console/**")
                        .permitAll()
                        // 통계는 관리자만
                        .requestMatchers("/api/*/posts/statistics")
                        .hasAuthority("ADMIN")
                        // API 요청은 인증 필요
                        .requestMatchers("/api/*/**")
                        .authenticated()
                        // 나머지는 모두 허용
                        .anyRequest()
                        .permitAll()
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
                                                         RespData.of("401-1", "권한이 없습니다.", null)
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
                                                            RespData.of("403-1", "접근 권한이 없습니다.", null)
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

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 허용할 오리진 설정
        configuration.setAllowedOrigins(Arrays.asList("https://cdpn.io", AppConfig.getSiteFrontUrl()));

        // 허용할 HTTP 메서드 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        // 자격 증명 허용 설정
        configuration.setAllowCredentials(true);

        // 허용할 헤더 설정
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // CORS 설정을 소스에 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);

        return source;
    }



}