package com.krstudy.kapi.com.krstudy.kapi.standard.base

import java.io.IOException

object Ut {
    object ThreadUtil {
        @Throws(InterruptedException::class)
        fun sleep(millis: Long) {
            Thread.sleep(millis)
        }
    }

    object Cmd {
        fun runAsync(cmd: String) {
            Thread {
                run(cmd)
            }.start()
        }

        fun run(cmd: String) {
            try {
                val processBuilder = ProcessBuilder("bash", "-c", cmd)
                val process = processBuilder.start()
                process.waitFor()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    object Url {
        fun modifyQueryParam(url: String, paramName: String, paramValue: String): String {
            var modifiedUrl = deleteQueryParam(url, paramName)
            modifiedUrl = addQueryParam(modifiedUrl, paramName, paramValue)
            return modifiedUrl
        }

        fun addQueryParam(url: String, paramName: String, paramValue: String): String {
            var updatedUrl = url
            if (!updatedUrl.contains("?")) {
                updatedUrl += "?"
            }
            if (!updatedUrl.endsWith("?") && !updatedUrl.endsWith("&")) {
                updatedUrl += "&"
            }
            updatedUrl += "$paramName=$paramValue"
            return updatedUrl
        }

        fun deleteQueryParam(url: String, paramName: String): String {
            val startPoint = url.indexOf("$paramName=")
            if (startPoint == -1) return url

            val endPoint = url.substring(startPoint).indexOf("&")
            return if (endPoint == -1) {
                url.substring(0, startPoint - 1)
            } else {
                val urlAfter = url.substring(startPoint + endPoint + 1)
                url.substring(0, startPoint) + urlAfter
            }
        }
    }

    object Json {
        @Throws(IOException::class)
        fun toString(obj: Any): String {
            return AppConfig.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj)
        }
    }
}