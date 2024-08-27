package com.krstudy.kapi.global.https

import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.Security.SecurityUser
import com.krstudy.kapi.standard.base.Ut
import jakarta.persistence.EntityManager
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Component
@RequestScope
@RequiredArgsConstructor
class ReqData(
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val entityManager: EntityManager
) {
    private var member: Member? = null

    fun redirect(url: String, msg: String): String {
        val urlBits = url.split("#", limit = 2)
        val newUrl = urlBits[0]
        val encodedMsg = URLEncoder.encode(msg, StandardCharsets.UTF_8)

        val sb = StringBuilder()
        sb.append("redirect:")
        sb.append(newUrl)

        if (encodedMsg.isNotEmpty()) {
            sb.append("?msg=")
            sb.append(encodedMsg)
        }

        if (urlBits.size == 2) {
            sb.append("#")
            sb.append(urlBits[1])
        }

        return sb.toString()
    }

    fun historyBack(msg: String): String {
        request.setAttribute("failMsg", msg)
        return "global/js"
    }

    fun redirectOrBack(rs: RespData<*>, path: String): String {
        return if (rs.isFail()) historyBack(rs.msg) else redirect(path, rs.msg)
    }

    fun getUser(): SecurityUser? {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal

        println("Authentication: $authentication")
        println("Principal: $principal")

        return if (principal is SecurityUser) {
            principal
        } else {
            null
        }
    }


    fun isLogin(): Boolean {
        return getUser() != null
    }

    fun isLogout(): Boolean {
        return !isLogin()
    }

    fun isAdmin(): Boolean {
        if (isLogout()) return false

        return getUser()?.authorities?.any { it.authority == "ROLE_ADMIN" } == true
    }

    fun setAttribute(key: String, value: Any) {
        request.setAttribute(key, value)
    }

    fun getCurrentQueryStringWithoutParam(paramName: String): String {
        val queryString = request.queryString ?: return ""
        return Ut.deleteQueryParam(queryString, paramName)
    }

    fun getMember(): Member? {
        if (isLogout()) return null

        if (member == null) {
            member = entityManager.getReference(Member::class.java, getUser()?.id)
        }

        return member
    }
}