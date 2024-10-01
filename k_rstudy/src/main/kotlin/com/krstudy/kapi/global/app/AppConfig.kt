package com.krstudy.kapi.global.app

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class AppConfig {

    companion object {
        var jwtSecretKey: String? = null
            private set

        var tempDirPath: String? = null
            private set

        var genFileDirPath: String? = null
            private set

        var siteName: String? = null
            private set

        var objectMapper: ObjectMapper? = null
            private set

        var basePageSize: Int = 10
            private set

        private var accessTokenExpirationSec: Long = 0
        private var siteFrontUrl: String? = null
        private var siteBackUrl: String? = null
        var siteCookieDomain: String? = null
            internal set

        private var resourcesStaticDirPath: String? = null

        // Getter 메소드
        fun getJwtSecretKeyOrThrow(): String {
            return jwtSecretKey ?: throw IllegalStateException("JWT secret key is not initialized")
        }

        fun getAccessTokenExpirationSec(): Long {
            return accessTokenExpirationSec
        }

        fun getResourcesStaticDirPath(): String {
            if (resourcesStaticDirPath == null) {
                val resource = ClassPathResource("static/")
                resourcesStaticDirPath = try {
                    resource.file.absolutePath
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
            return resourcesStaticDirPath!!
        }

        fun getSiteFrontUrl(): String? {
            return siteFrontUrl
        }
    }

    @Value("\${custom.jwt.secretKey}")
    fun setJwtSecretKey(jwtSecretKey: String) {
        Companion.jwtSecretKey = jwtSecretKey
    }

    @Value("\${custom.accessToken.expirationSec}")
    fun setAccessTokenExpirationSec(accessTokenExpirationSec: Long) {
        Companion.accessTokenExpirationSec = accessTokenExpirationSec
    }

    @Value("\${custom.site.frontUrl}")
    fun setSiteFrontUrl(siteFrontUrl: String) {
        Companion.siteFrontUrl = siteFrontUrl
    }

    @Value("\${custom.site.backUrl}")
    fun setSiteBackUrl(siteBackUrl: String) {
        Companion.siteBackUrl = siteBackUrl
    }

    @Value("\${custom.site.cookieDomain}")
    fun setSiteCookieDomain(siteCookieDomain: String) {
        Companion.siteCookieDomain = siteCookieDomain
    }

    @Value("\${custom.temp.dirPath}")
    fun setTempDirPath(tempDirPath: String) {
        Companion.tempDirPath = tempDirPath
    }

    @Value("\${custom.genFile.dirPath}")
    fun setGenFileDirPath(genFileDirPath: String) {
        Companion.genFileDirPath = genFileDirPath
    }

    @Value("\${custom.site.name}")
    fun setSiteName(siteName: String) {
        Companion.siteName = siteName
    }

    @Autowired
    fun setObjectMapper(objectMapper: ObjectMapper) {
        Companion.objectMapper = objectMapper
    }
}