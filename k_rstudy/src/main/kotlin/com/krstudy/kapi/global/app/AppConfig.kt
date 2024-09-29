package com.krstudy.kapi.global.app

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.web.context.request.RequestContextListener
import java.io.IOException


@Configuration
class AppConfig {

    companion object {
        private var resourcesStaticDirPath: String? = null

        @JvmStatic
        var tempDirPath: String? = null
            private set

        @JvmStatic
        var genFileDirPath: String? = null
            private set

        @JvmStatic
        var siteName: String? = null
            private set

        @JvmStatic
        var siteBaseUrl: String? = null
            private set

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
    }

    @Value("\${custom.tempDirPath}")
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

    @Value("\${custom.site.baseUrl}")
    fun setSiteBaseUrl(siteBaseUrl: String) {
        Companion.siteBaseUrl = siteBaseUrl
    }

    @Bean
    fun requestContextListener(): RequestContextListener {
        return RequestContextListener()
    }
}