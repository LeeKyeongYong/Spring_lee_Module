package com.krstudy.kapi.global.Security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke

@Configuration
@EnableWebSecurity
class PopupSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                // 공개 접근 허용
                authorize("/api/public/**", permitAll)

                // 팝업 관리자 권한 필요
                authorize("/api/admin/popups/**", hasRole("ROLE_ADMIN"))

                // 템플릿 관리자 권한 필요
                authorize("/api/admin/popup-templates/**", hasRole("ROLE_ADMIN"))

                // 그 외 요청은 인증 필요
                authorize(anyRequest, authenticated)
            }

            csrf {
                // CSRF 보호 예외 설정
                disable()  // 또는 필요한 경로만 제외하려면:
                // ignoringRequestMatchers("/api/public/**")
            }

            // 필요한 경우 추가 보안 설정
            headers {
                frameOptions {
                    sameOrigin = true
                }
            }

            cors {
                disable()  // 또는 필요한 CORS 설정 추가
            }
        }

        return http.build()
    }
}