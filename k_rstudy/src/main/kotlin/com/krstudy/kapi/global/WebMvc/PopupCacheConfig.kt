package com.krstudy.kapi.global.WebMvc

@Configuration
class PopupCacheConfig {
    @Bean
    fun popupCache(): Cache<Long, PopupEntity> {
        return Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build()
    }
}