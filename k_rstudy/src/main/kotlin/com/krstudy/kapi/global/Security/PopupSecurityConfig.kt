package com.krstudy.kapi.global.Security

@Configuration
@EnableWebSecurity
class PopupSecurityConfig {
    @Bean
    fun popupSecurityFilter(): SecurityFilterChain {
        return http {
            authorizeRequests {
                // 팝업 관리 권한 설정
                request("/api/admin/popups/**") {
                    hasRole("POPUP_ADMIN")
                }
                // 템플릿 관리 권한 설정
                request("/api/admin/popup-templates/**") {
                    hasRole("TEMPLATE_ADMIN")
                }
            }
            // CSRF 보호
            csrf {
                ignoringAntMatchers("/api/public/**")
            }
        }
    }
}