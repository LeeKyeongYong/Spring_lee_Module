package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.AuthTokenService
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.global.Security.handler.CustomAuthenticationSuccessHandler
import com.krstudy.kapi.global.Security.handler.CustomOAuth2FailureHandler
import com.krstudy.kapi.global.Security.service.CustomUserDetailsService
import com.krstudy.kapi.global.https.ReqData
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.http.HttpMethod

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val authTokenService: AuthTokenService,
    private val rq: ReqData,
    @Lazy private val customUserDetailsService: CustomUserDetailsService,
    private val customOAuth2FailureHandler: CustomOAuth2FailureHandler
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
    fun authenticationProvider(): AuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(customUserDetailsService)
        provider.setPasswordEncoder(passwordEncoder())
        return provider
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    // 기존 권한 설정
                    .requestMatchers("/adm/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/api/banners/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/resource/**").permitAll()
                    .requestMatchers("/v1/qrcode/**").permitAll()
                    .requestMatchers("/api/**").permitAll()
                    .requestMatchers("/v1/**").authenticated()
                    .requestMatchers("/login/oauth2/**").permitAll()
                    .requestMatchers("/member/login").anonymous()
                    .requestMatchers("/member/join").hasAnyRole("ADMIN")
                    .requestMatchers("/image/**").permitAll()
                    .requestMatchers("/api/messages/**").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/v1/chat/rooms").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/chat/rooms").authenticated()

                    // 팝업 관련 권한 설정 추가
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/api/admin/popups/**").hasAuthority("ROLE_ADMIN")
                    .requestMatchers("/api/admin/popup-templates/**").hasAuthority("ROLE_ADMIN")

                    .anyRequest().permitAll()
            }
            .headers { headers ->
                headers.frameOptions { frameOptions ->
                    frameOptions.sameOrigin()
                }
            }
            .csrf { csrf ->
                csrf
                    .ignoringRequestMatchers(
                        "/v1/**",
                        "/api/**",
                        "/ws/**",
                        "/api/public/**"
                    )
            }
            .cors { cors ->
                cors.disable()
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/member/login")
                    .failureUrl("/?msg=" + URLEncoder.encode("로그인에 실패하였습니다.", StandardCharsets.UTF_8))
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
                    .expiredUrl("/?msg=" + URLEncoder.encode("세션이 만료되었습니다.", StandardCharsets.UTF_8))
            }
            .oauth2Login { oauth2Login ->
                oauth2Login
                    .successHandler(customAuthSuccessHandler())
                    .failureHandler(customOAuth2FailureHandler)
                    .defaultSuccessUrl("/", true)
            }
            .authenticationProvider(authenticationProvider())

        return http.build()
    }
}