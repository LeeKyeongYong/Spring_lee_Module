package com.krstudy.kapi.com.krstudy.kapi.standard.base


object Ut {
    object Url {
        fun modifyQueryParam(url: String, paramName: String, paramValue: String): String {
            var modifiedUrl = deleteQueryParam(url, paramName)
            modifiedUrl = addQueryParam(modifiedUrl, paramName, paramValue)
            return modifiedUrl
        }

        fun addQueryParam(url: String, paramName: String, paramValue: String): String {
            val separator = if (url.contains("?")) "&" else "?"
            return "$url$separator$paramName=$paramValue"
        }

        fun deleteQueryParam(url: String, paramName: String): String {
            val paramPrefix = "$paramName="
            val startPoint = url.indexOf(paramPrefix)
            if (startPoint == -1) return url

            val endPoint = url.indexOf('&', startPoint)
            return if (endPoint == -1) {
                url.removeRange(startPoint - 1, url.length)
            } else {
                url.removeRange(startPoint, endPoint) + url.substring(endPoint + 1)
            }
        }
    }
}

