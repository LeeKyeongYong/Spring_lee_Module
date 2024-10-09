package com.krstudy.kapi.global.app

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver  // WebMVC의 PathResourceResolver 사용
import java.util.concurrent.TimeUnit

@Configuration
class WebConfig : WebMvcConfigurer {
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
        registry.addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/css/")
            .setCachePeriod(3600)
            .resourceChain(true)
            .addResolver(PathResourceResolver())

        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
            .setCachePeriod(3600)
            .resourceChain(true)
            .addResolver(PathResourceResolver())

        registry.addResourceHandler("/resource/**")
            .addResourceLocations("classpath:/static/resource/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
            .resourceChain(true)
    }
}