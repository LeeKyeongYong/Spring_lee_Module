package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.AuthTokenService
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.https.ReqData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
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
class SecurityConfig(
    private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler,
    private val authTokenService: AuthTokenService,  // 추가된 authTokenService
    private val rq: ReqData                               // 추가된 rq
) {

    @Autowired
    @Lazy
    private lateinit var memberService: MemberService

    @Bean
    fun customAuthSuccessHandler(): AuthenticationSuccessHandler {
        return CustomAuthenticationSuccessHandler(memberService, rq, authTokenService)  // 인자 순서 수정
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/adm/**").hasRole("ADMIN")
                    .requestMatchers("/v1/qrcode/**").permitAll()
                    .requestMatchers("/v1/**").authenticated()
                    .requestMatchers("/member/login").anonymous()
                    .requestMatchers("/member/join")
                    .access("hasRole('ADMIN') or isAnonymous()")
                    .requestMatchers("/image/**").permitAll()
                    .anyRequest().permitAll()
            }
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.sameOrigin()
                }
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/v1/**")
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/member/login")
                    .successHandler(customAuthSuccessHandler())  // 수정된 부분
                    .defaultSuccessUrl("/?msg=" + URLEncoder.encode("환영합니다.", StandardCharsets.UTF_8))
                    .failureHandler(customAuthenticationFailureHandler)
            }
            .logout { logout ->
                logout.logoutRequestMatcher(AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/?msg=" + URLEncoder.encode("로그아웃되었습니다.", StandardCharsets.UTF_8))
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
            .sessionManagement { sessionManagement ->
                sessionManagement
                    .invalidSessionUrl("/member/login?failMsg=" + URLEncoder.encode("세션이 만료되었습니다.", StandardCharsets.UTF_8))
                    .maximumSessions(1)
                    .expiredUrl("/member/login?failMsg=" + URLEncoder.encode("세션이 만료되었습니다.", StandardCharsets.UTF_8))
            }
            .oauth2Login { oauth2Login ->
                oauth2Login.successHandler(customAuthSuccessHandler())  // 수정된 부분
            }

        return http.build()
    }
}