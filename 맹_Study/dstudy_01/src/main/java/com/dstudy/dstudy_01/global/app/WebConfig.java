package com.dstudy.dstudy_01.global.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/previews/**")
                .addResourceLocations("file:upload-dir/previews/");
        registry.addResourceHandler("/documents/**")
                .addResourceLocations("file:upload-dir/documents/");
    }
}