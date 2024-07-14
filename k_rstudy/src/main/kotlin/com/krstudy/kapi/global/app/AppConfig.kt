package com.krstudy.kapi.global.app

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class AppConfig(
    @Value("\${custom.jwt.secretKey}") val jwtSecretKey: String,
    @Value("\${custom.accessToken.expirationSec}") val accessTokenExpirationSec: Long,
    @Value("\${custom.site.frontUrl}") val siteFrontUrl: String,
    @Value("\${custom.site.backUrl}") val siteBackUrl: String,
    @Value("\${custom.site.cookieDomain}") val siteCookieDomain: String,
    @Value("\${custom.temp.dirPath}") val tempDirPath: String,
    @Value("\${custom.genFile.dirPath}") val genFileDirPath: String,
    @Value("\${custom.site.name}") val siteName: String,
    val objectMapper: ObjectMapper
) {

    companion object {
        private var resourcesStaticDirPath: String? = null

        @JvmStatic
        fun getResourcesStaticDirPath(): String {
            if (resourcesStaticDirPath == null) {
                val resource = ClassPathResource("static/")
                try {
                    resourcesStaticDirPath = resource.file.absolutePath
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
            return resourcesStaticDirPath!!
        }

        @JvmStatic
        var basePageSize: Int = 10
    }
}
