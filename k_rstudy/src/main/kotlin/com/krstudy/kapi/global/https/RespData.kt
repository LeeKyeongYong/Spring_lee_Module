package com.krstudy.kapi.global.https

data class RespData<T>(
    val resultCode: String,
    val statusCode: Int,
    val msg: String,
    val data: T?
) {
    companion object {
        @JvmStatic
        fun <T> of(resultCode: String, msg: String): RespData<T> {
            return of(resultCode, msg, null)
        }

        @JvmStatic
        fun <T> of(resultCode: String, msg: String, data: T?): RespData<T> {
            val statusCode = resultCode.split("-", limit = 2)[0].toInt()
            return RespData(resultCode, statusCode, msg, data)
        }
    }

    fun isSuccess(): Boolean {
        return statusCode in 200 until 400
    }

    fun isFail(): Boolean {
        return !isSuccess()
    }
}