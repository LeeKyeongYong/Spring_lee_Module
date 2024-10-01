package com.krstudy.kapi.global.Security

import com.krstudy.kapi.domain.member.service.MemberService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
@Configuration
@EnableMethodSecurity
class SecurityConfig (
    private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler,
    private val customOAuth2UserService: CustomOAuth2UserService

){

    @Autowired
    @Lazy
    private lateinit var memberService: MemberService

    @Bean
    fun customAuthenticationSuccessHandler(): AuthenticationSuccessHandler {
        return CustomAuthenticationSuccessHandler(memberService)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    private fun configureOAuth2Login(oauth2LoginConfigurer: OAuth2LoginConfigurer<HttpSecurity>) {
        oauth2LoginConfigurer.userInfoEndpoint { endpointCustomizer ->
            endpointCustomizer.userService(customOAuth2UserService)
        }
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
                    .access("hasRole('ADMIN') or isAnonymous()") // 관리자나 비로그인 사용자만 접근 가능
                    .requestMatchers("/image/**").permitAll() // 이미지 경로에 대한 접근 허용
                    .anyRequest().permitAll()
            }
            .csrf { csrf ->
                csrf
                    .ignoringRequestMatchers("/v1/**")
                    .ignoringRequestMatchers("/member/join")
            }
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/member/login")
                    .successHandler(customAuthenticationSuccessHandler())
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


        return http.build()
    }

}