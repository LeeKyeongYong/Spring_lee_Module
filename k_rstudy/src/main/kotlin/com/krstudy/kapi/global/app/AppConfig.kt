package com.krstudy.kapi.global.app

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class AppConfig {

    var jwt: JwtConfig? = null

    // Inner class to hold JWT configuration
    class JwtConfig {
        var secretKey: String? = null
        var expirationSec: Long? = null
    }

    companion object {
        lateinit var jwtSecretKey: String
        lateinit var tempDirPath: String
        lateinit var genFileDirPath: String
        lateinit var siteName: String
        lateinit var objectMapper: ObjectMapper
        var basePageSize: Int = 10
        var accessTokenExpirationSec: Long = 0 // getter가 자동 생성됨
        lateinit var siteCookieDomain: String
        private var resourcesStaticDirPath: String? = null
        private var siteBackUrl: String? = null
        @JvmStatic
        lateinit var siteFrontUrl: String

        // Getter for siteFrontUrl
        fun getSiteFrontUrl(): String {
            return siteFrontUrl
        }

        fun getJwtSecretKeyOrThrow(): String {
            return jwtSecretKey ?: throw IllegalStateException("JWT secret key is not initialized")
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
    }

    @Value("\${custom.jwt.secretKey}")
    fun setJwtSecretKey(jwtSecretKey: String) {
        Companion.jwtSecretKey = jwtSecretKey
    }

    @Value("\${custom.accessToken.expirationSec}")
    fun setAccessTokenExpirationSec(accessTokenExpirationSec: Long) {
        Companion.accessTokenExpirationSec = accessTokenExpirationSec
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

    @Value("\${custom.dev.backUrl}")
    fun setSiteFrontUrl(siteFrontUrl: String) {
        Companion.siteFrontUrl = siteFrontUrl
    }

    @Autowired
    fun setObjectMapper(objectMapper: ObjectMapper) {
        Companion.objectMapper = objectMapper
    }
}