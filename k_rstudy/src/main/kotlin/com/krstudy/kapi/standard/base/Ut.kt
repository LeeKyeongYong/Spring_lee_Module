package com.krstudy.kapi.standard.base

class Ut {
    companion object {
        fun modifyQueryParam(url: String, paramName: String, paramValue: String): String {
            var updatedUrl = deleteQueryParam(url, paramName)
            updatedUrl = addQueryParam(updatedUrl, paramName, paramValue)
            return updatedUrl
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
            val paramWithEqualSign = "$paramName="
            val startPoint = url.indexOf(paramWithEqualSign)
            if (startPoint == -1) return url

            val endPoint = url.indexOf("&", startPoint)
            return if (endPoint == -1) {
                url.substring(0, startPoint - 1).takeIf { it.isNotEmpty() } ?: url
            } else {
                url.substring(0, startPoint) + url.substring(endPoint + 1)
            }
        }
    }
}
