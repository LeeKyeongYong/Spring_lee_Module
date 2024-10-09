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
    private val authTokenService: AuthTokenService,
    private val rq: ReqData
) {
    @Autowired
    @Lazy
    private lateinit var memberService: MemberService

    @Bean
    fun customAuthSuccessHandler(): AuthenticationSuccessHandler {
        return CustomAuthenticationSuccessHandler(memberService, rq, authTokenService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/adm/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/resource/**").permitAll()
                    .requestMatchers("/v1/qrcode/**").permitAll()
                    .requestMatchers("/v1/**").authenticated()
                    .requestMatchers("/login/oauth2/**").permitAll()
                    .requestMatchers("/member/login").anonymous()
                    .requestMatchers("/member/join").hasAnyRole("ADMIN") // ADMIN 권한이 있는 사용자만 허용
                    .requestMatchers("/image/**").permitAll()
                    .requestMatchers("/api/messages/**").authenticated()
                    .anyRequest().permitAll()
            }
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.sameOrigin()
                }
            }
            .csrf { csrf ->
                csrf.ignoringRequestMatchers("/v1/**", "/api/**")
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/member/login")
                    .failureUrl("/member/login?failMsg=" + URLEncoder.encode("로그인에 실패하였습니다.", StandardCharsets.UTF_8))
                    .successHandler(customAuthSuccessHandler())
                    .defaultSuccessUrl("/?msg=" + URLEncoder.encode("환영합니다.", StandardCharsets.UTF_8))
            }
            .logout { logout ->
                logout.logoutRequestMatcher(AntPathRequestMatcher("/member/logout"))
                    .logoutSuccessUrl("/?msg=" + URLEncoder.encode("로그아웃되었습니다.", StandardCharsets.UTF_8))
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
            .sessionManagement { sessionManagement ->
                sessionManagement
                    .invalidSessionUrl("/")
                    .maximumSessions(1)
                    .expiredUrl("/member/login?failMsg=" + URLEncoder.encode("세션이 만료되었습니다.", StandardCharsets.UTF_8))
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .successHandler(customAuthSuccessHandler())
                    .defaultSuccessUrl("/", true)
            }

        return http.build()
    }
}
