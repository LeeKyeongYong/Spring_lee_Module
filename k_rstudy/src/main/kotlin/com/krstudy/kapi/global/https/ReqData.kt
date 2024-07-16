package com.krstudy.kapi.com.krstudy.kapi.global.https

import com.krstudy.kapi.com.krstudy.kapi.standard.base.Ut
import jakarta.persistence.EntityManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Optional

@Component
@RequestScope
class ReqData(
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val entityManager: EntityManager
) {

    private var member: Member? = null

    fun redirect(url: String, msg: String): String {
        val urlBits = url.split("#", limit = 2)
        var url = urlBits[0]
        val encodedMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8.toString())

        val sb = StringBuilder()
        sb.append("redirect:$url")

        if (msg.isNotEmpty()) {
            sb.append("?msg=").append(encodedMsg)
        }

        if (urlBits.size == 2) {
            sb.append("#").append(urlBits[1])
        }

        return sb.toString()
    }

    fun historyBack(msg: String): String {
        request.setAttribute("failMsg", msg)
        return "global/js"
    }

    fun redirectOrBack(rs: RespData<*>, path: String): String {
        return if (rs.isFail) historyBack(rs.msg) else redirect(path, rs.msg)
    }

    fun getUser(): SecurityUser? {
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .filter { it is SecurityUser }
            .map { it as SecurityUser }
            .orElse(null)
    }

    fun isLogin(): Boolean = getUser() != null

    fun isLogout(): Boolean = !isLogin()

    fun isAdmin(): Boolean {
        return if (isLogout()) {
            false
        } else {
            getUser()?.authorities?.any { it.authority == "ROLE_ADMIN" } == true
        }
    }

    fun setAttribute(key: String, value: Any) {
        request.setAttribute(key, value)
    }

    fun getCurrentQueryStringWithoutParam(paramName: String): String {
        val queryString = request.queryString ?: return ""
        return Ut.url.deleteQueryParam(queryString, paramName)
    }

    fun getMember(): Member? {
        if (isLogout()) return null

        if (member == null) {
            member = entityManager.getReference(Member::class.java, getUser()?.id)
        }

        return member
    }
}