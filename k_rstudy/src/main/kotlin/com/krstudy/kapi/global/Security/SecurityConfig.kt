package com.krstudy.kapi.global.Security

import com.krstudy.kapi.com.krstudy.kapi.global.Security.CustomAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Configuration
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests { authorizeRequests  ->
                authorizeRequests
                    .requestMatchers("/adm/**").hasRole("ADMIN")
                    /*
                     MEMBER("ROLE_MEMBER"),
                     ADMIN("ROLE_ADMIN"),
                     HEADHUNTER("ROLE_HEADHUNTER"),
                     MANAGER("ROLE_MANAGER"),
                     HR("ROLE_HR");
                     */
                    //.requestMatchers("/adm/**").hasRole("ROLE_ADMIN")
                    .requestMatchers("/v1/qrcode/**").permitAll() // 로그인 없이 접근 허용
                    .requestMatchers("/v1/**").authenticated() // 인증된 사용자만 접근 가능
                    .anyRequest().permitAll()
            }
            .csrf { csrf ->
                csrf
                    .ignoringRequestMatchers("/v1/**") // CSRF 보호 비활성화
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/member/login")
                    .successHandler(customAuthenticationSuccessHandler()) // 커스터마이즈된 핸들러 적용
                    .defaultSuccessUrl("/?msg=" + URLEncoder.encode("환영합니다.", StandardCharsets.UTF_8))
                    .failureUrl("/member/login?failMsg=" + URLEncoder.encode("아이디 또는 비밀번호가 틀렸습니다.", StandardCharsets.UTF_8))
            }

            .logout { logout ->
                logout.logoutRequestMatcher(AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/?msg=" + URLEncoder.encode("로그아웃되었습니다.", StandardCharsets.UTF_8))
                    .invalidateHttpSession(true) // 세션 무효화
                    .deleteCookies("JSESSIONID") // 쿠키 삭제
            }.sessionManagement { sessionManagement ->
                sessionManagement
                    .invalidSessionUrl("/member/login?failMsg=" + URLEncoder.encode("세션이 만료되었습니다.", StandardCharsets.UTF_8))
                    .maximumSessions(1)
                    .expiredUrl("/member/login?failMsg=" + URLEncoder.encode("세션이 만료되었습니다.", StandardCharsets.UTF_8))
            }

        return http.build()
    }

    @Bean
    fun customAuthenticationSuccessHandler(): AuthenticationSuccessHandler {
        return CustomAuthenticationSuccessHandler()
    }

}