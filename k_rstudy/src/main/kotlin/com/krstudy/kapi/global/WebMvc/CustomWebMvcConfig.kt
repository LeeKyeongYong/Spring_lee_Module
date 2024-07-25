package com.krstudy.kapi.com.krstudy.kapi.global.WebMvc

import com.krstudy.kapi.global.app.AppConfig
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CustomWebMvcConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins("https://cdpn.io", "http://localhost:5173")
            .allowedMethods("*")
            .allowedHeaders("*")
            .allowCredentials(true)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        val genFileDirPath = AppConfig.genFileDirPath
            ?: throw IllegalStateException("genFileDirPath is not initialized")

        registry.addResourceHandler("/gen/**")
            .addResourceLocations("file:///$genFileDirPath/")
    }
}