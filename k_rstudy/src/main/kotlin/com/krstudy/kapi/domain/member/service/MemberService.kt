package com.krstudy.kapi.domain.member.service

import io.jsonwebtoken.SignatureAlgorithm
import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.global.exception.ErrorCode
import com.krstudy.kapi.global.https.RespData
import io.jsonwebtoken.Jwts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import org.springframework.beans.factory.annotation.Value
@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Value("\${security.jwt.secret}")
    private lateinit var secretKey: String
    @Transactional
    suspend fun join(userid: String, username: String, userEmail: String, password: String, imageType: String?, imageBytes: ByteArray?): RespData<Member> {
        val existingMember = findByUserid(userid)
        if (existingMember != null) {
            return RespData.fromErrorCode(ErrorCode.DUPLICATED_USERID) // 새로운 에러 코드 추가
        //return RespData.fromErrorCode(ErrorCode.UNAUTHORIZED)
        }

        // 역할을 결정하는 로직
        val roleType = M_Role.values().find { it.authority.equals(userid, ignoreCase = true) }?.authority
            ?: run {
                if (userid.equals("system", ignoreCase = true) || userid.equals("admin", ignoreCase = true)) {
                    M_Role.ADMIN.authority
                } else {
                    M_Role.MEMBER.authority
                }
            }

        // JWT 생성
        val token = generateJwtToken(userid, secretKey)

        val member = Member().apply {
            this.userid = userid
            this.username = username
            this.userEmail = userEmail
            this.password = passwordEncoder.encode(password)
            this.roleType = roleType
            this.useYn = "Y"
            this.jwtToken = token
            this.imageType = imageType // 이미지 타입 저장
            this.image = imageBytes // 이미지 바이트 저장
        }

        withContext(Dispatchers.IO) {
            println("Insert method called with data: $member") // 로그 추가
            memberRepository.save(member)
        }

        return RespData.of(
            ErrorCode.SUCCESS.code,
            "${member.userid}님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.",
            member
        )
    }

    fun findByUserid(userid: String): Member? {
        return memberRepository.findByUserid(userid)
    }

    fun findByUserName(username: String): Member {
        return memberRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
    }
    fun count(): Long {
        return memberRepository.count()
    }

    private fun generateJwtToken(userid: String, secretKey: String): String {
        return Jwts.builder()
            .setSubject(userid)
            .setExpiration(Date(System.currentTimeMillis() + 600000))  // 1분 유효
            .signWith(SignatureAlgorithm.HS512, secretKey.toByteArray())
            .compact()
    }

    fun getImageByNo(id: Long): Member? {
        return getMemberByNo(id)
    }

    fun getMemberByNo(id: Long): Member? {
        return memberRepository.findById(id).orElse(null)
    }

}