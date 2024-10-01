package com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.domain.comment.repository.PostCommentRepository
import io.jsonwebtoken.SignatureAlgorithm
import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.oauth2.entity.Social
import com.krstudy.kapi.domain.post.repository.PostRepository
import com.krstudy.kapi.domain.post.repository.PostlikeRepository
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
    private val passwordEncoder: PasswordEncoder,
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository,
    private val postlikeRepository: PostlikeRepository
) {
    @Value("\${security.jwt.secret}")
    private lateinit var secretKey: String
    @Transactional
    suspend fun join(userid: String, username: String, userEmail: String, password: String, imageType: String?, imageBytes: ByteArray?, roleType: String?, social: Social?): RespData<Member> {
        val existingMember = findByUserid(userid)
        if (existingMember != null) {
            return RespData.fromErrorCode(ErrorCode.DUPLICATED_USERID)
        }

        // 역할을 결정하는 로직
        val finalRoleType = when {
            userid.equals("system", ignoreCase = true) || userid.equals("admin", ignoreCase = true) -> {
                M_Role.ADMIN.authority
            }
            roleType.isNullOrEmpty() -> {
                M_Role.MEMBER.authority
            }
            else -> {
                M_Role.values().find { it.authority.equals(roleType, ignoreCase = true) }?.authority ?: M_Role.MEMBER.authority
            }
        }

        // JWT 생성
        val token = generateJwtToken(userid, secretKey)

        val member = Member().apply {
            this.userid = userid
            this.username = username
            this.userEmail = userEmail
            this.password = passwordEncoder.encode(password)
            this.roleType = finalRoleType
            this.useYn = "Y"
            this.jwtToken = token
            this.imageType = imageType
            this.image = imageBytes
            this.social = social // 사용자가 제공한 social 값을 설정
        }

        // social이 null일 경우 WEB으로 설정
        if (member.social == null) {
            member.social = Social.WEB
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

    @Transactional(readOnly = true)
    fun getMemberByNo(id: Long): Member? {
        return memberRepository.findById(id).orElse(null) // ID 타입이 Long이어야 합니다.
    }

    @Transactional(readOnly = true)
    fun getAllMembers(): List<Member> {
        return memberRepository.findAll()
    }

    @Transactional
    fun removeMember(id: Long) {
        memberRepository.deleteById(id) // ID에 해당하는 멤버 삭제
        postRepository.deleteByAuthorId(id)
        postCommentRepository.deleteByAuthorId(id)
    }

}