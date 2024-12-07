package com.krstudy.kapi.global.app

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import java.util.concurrent.TimeUnit
import org.springframework.web.servlet.resource.VersionResourceResolver

@Configuration
class WebConfig (
    @Value("\${spring.application.version}") private val appVersion: String
): WebMvcConfigurer {

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // Global Resource 처리
        registry.addResourceHandler("/global/**")
            .addResourceLocations("classpath:/static/resource/global/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .resourceChain(true)
            .addResolver(VersionResourceResolver().addContentVersionStrategy("/**"))
            .addResolver(PathResourceResolver())

        // Vendors Resource 처리
        registry.addResourceHandler("/vendors/**")
            .addResourceLocations("classpath:/static/resource/global/vendors/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .resourceChain(true)
            .addResolver(PathResourceResolver())

        // Images 처리
        registry.addResourceHandler("/images/**")
            .addResourceLocations("file:gen/images/", "classpath:/gen/images/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .resourceChain(true)
            .addResolver(PathResourceResolver())
    }

    @Bean
    fun resourceVersioning(): String = appVersion
}