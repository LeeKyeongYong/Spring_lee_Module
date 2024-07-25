package com.krstudy.kapi.global.app

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.IOException

@Configuration
class AppConfig {

    @Value("\${custom.tempDirPath}")
    lateinit var tempDirPath: String

    @Value("\${custom.genFile.dirPath}")
    lateinit var genFileDirPath: String

    @Value("\${custom.site.name}")
    lateinit var siteName: String

    @Value("\${custom.site.baseUrl}")
    lateinit var siteBaseUrl: String

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
    }
}
