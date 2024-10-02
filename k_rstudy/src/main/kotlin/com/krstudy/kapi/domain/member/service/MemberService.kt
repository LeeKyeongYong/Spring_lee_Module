package com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.domain.comment.repository.PostCommentRepository
import com.krstudy.kapi.domain.member.datas.AuthAndMakeTokensResponseBody
import io.jsonwebtoken.SignatureAlgorithm
import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.datas.RegistrationData
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.post.repository.PostRepository
import com.krstudy.kapi.domain.post.repository.PostlikeRepository
import com.krstudy.kapi.global.exception.CustomException
import com.krstudy.kapi.global.exception.ErrorCode
import com.krstudy.kapi.global.exception.GlobalException
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
    private val postlikeRepository: PostlikeRepository,
    private val authTokenService: AuthTokenService
) {
    @Value("\${security.jwt.secret}")
    private lateinit var secretKey: String
    @Transactional
    suspend fun join(userid: String, username: String, userEmail: String, password: String, imageType: String?, imageBytes: ByteArray?, roleType: String?): RespData<Member> {
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    fun getMemberByNo(id: Long): Member? {
        return memberRepository.findById(id).orElse(null)
    }

    @Transactional(readOnly = true)
    fun getRegistrationDataByNo(id: Long): RegistrationData? {
        val member = memberRepository.findById(id).orElse(null)
        return member?.let {
            RegistrationData(
                userid = it.userid,
                username = it.username ?: "",
                password = it.password,
                userEmail = it.userEmail,
                imageType = it.imageType,
                imageBytes = it.image,
                additionalFields = emptyMap()
            )
        }
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


    fun passwordMatches(member: Member, password: String): Boolean {
        return passwordEncoder.matches(password, member.password)
    }

    @Transactional
    fun authAndMakeTokens(username: String, password: String): RespData<AuthAndMakeTokensResponseBody> {
        val member = findByUserName(username) ?: throw GlobalException("400-1", "해당 유저가 존재하지 않습니다.")

        if (!passwordMatches(member, password)) {
            throw GlobalException("400-2", "비밀번호가 일치하지 않습니다.")
        }

        val refreshToken = member.jwtToken
        val accessToken = authTokenService.genAccessToken(member)

        return RespData.of(
            ErrorCode.SUCCESS.code,  // resultCode에 SUCCESS 코드 추가
            "${member.username}님 안녕하세요.",
            AuthAndMakeTokensResponseBody(member, accessToken, refreshToken) // data에 ResponseBody 추가
        )
    }


}