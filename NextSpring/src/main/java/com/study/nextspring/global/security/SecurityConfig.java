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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.POST, "/api/v1/members/login").permitAll() // 정확한 로그인 엔드포인트 허용
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/me").permitAll() // 로그인 상태 확인 엔드포인트 허용
                                .requestMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                                .anyRequest().authenticated()
                )
                .headers(
                        headers -> headers.frameOptions(
                                frameOptions -> frameOptions.sameOrigin()
                        )
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(
                                (request, response, authException) -> {
                                    response.setContentType("application/json;charset=UTF-8");
                                    response.setStatus(403);
                                    response.setHeader("Access-Control-Allow-Credentials", "true");
                                    response.setHeader("Access-Control-Allow-Origin", AppConfig.getSiteFrontUrl()); // 클라이언트 URL
                                    response.getWriter().write(
                                            UtClass.json.toString(
                                                    RespData.of("403-1", request.getRequestURI() + ", " + authException.getLocalizedMessage())
                                            )
                                    );
                                }
                        ))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(
                        request -> {
                            CorsConfiguration corsConfig = new CorsConfiguration();
                            corsConfig.setAllowCredentials(true);
                            corsConfig.addAllowedOrigin(AppConfig.getSiteFrontUrl()); // 클라이언트 URL
                            corsConfig.addAllowedHeader("*");
                            corsConfig.addAllowedMethod("*");
                            return corsConfig;
                        }
                ));

        return http.build();
    }
}