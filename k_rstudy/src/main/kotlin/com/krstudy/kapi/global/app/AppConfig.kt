package com.krstudy.kapi.global.app

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class AppConfig {

    companion object {
        private var resourcesStaticDirPath: String? = null

        @JvmStatic
        @get:Value("\${custom.tempDirPath}")
        var tempDirPath: String? = null
            private set

        @JvmStatic
        @get:Value("\${custom.genFile.dirPath}")
        var genFileDirPath: String? = null
            private set

        @JvmStatic
        @get:Value("\${custom.site.name}")
        var siteName: String? = null
            private set

        @JvmStatic
        @get:Value("\${custom.site.baseUrl}")
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

        @JvmStatic
        fun getGenFileDirPath(): String {
            // Ensure that genFileDirPath is initialized
            return genFileDirPath ?: throw IllegalStateException("genFileDirPath is not initialized")
        }
    }
}
