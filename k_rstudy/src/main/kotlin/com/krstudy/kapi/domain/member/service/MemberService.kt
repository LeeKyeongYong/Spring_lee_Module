package com.krstudy.kapi.domain.member.service

import com.krstudy.kapi.domain.comment.repository.PostCommentRepository
import com.krstudy.kapi.domain.member.datas.AuthAndMakeTokensResponseBody
import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.datas.RegistrationData
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.domain.member.repository.MemberRepository
import com.krstudy.kapi.domain.post.repository.PostRepository
import com.krstudy.kapi.domain.post.repository.PostlikeRepository
import com.krstudy.kapi.global.Security.SecurityUser
import com.krstudy.kapi.global.exception.GlobalException
import com.krstudy.kapi.global.exception.MessageCode
import com.krstudy.kapi.global.https.RespData
import com.krstudy.kapi.member.datas.MemberUpdateData
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import java.util.*
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.support.TransactionTemplate
import org.slf4j.Logger


@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository,
    private val postlikeRepository: PostlikeRepository,
    private val authTokenService: AuthTokenService,
    private val transactionManager: PlatformTransactionManager
) {

    @PersistenceContext
    private lateinit var entityManager: EntityManager



    companion object {
        private val logger = LoggerFactory.getLogger(MemberService::class.java)
    }


    @Value("\${security.jwt.secret}")
    private lateinit var secretKey: String
    @Transactional
    suspend fun join(userid: String, username: String,nickname: String, userEmail: String, password: String, imageType: String?, imageBytes: ByteArray?, roleType: String?,profileImgUrl:String?,accountType:String): RespData<Member> {
        val existingMember = findByUserid(userid)
        if (existingMember != null) {
            return RespData.fromErrorCode(MessageCode.DUPLICATED_USERID)
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
            this.nickname = nickname
            this.userEmail = userEmail
            this.password = passwordEncoder.encode(password)
            this.roleType = finalRoleType
            this.useYn = "Y"
            this.jwtToken = token
            this.imageType = imageType
            this.image = imageBytes
            this.profileImgUrl=profileImgUrl
            this.accountType = if (accountType.isNullOrBlank()) "WEB" else accountType

        }



        withContext(Dispatchers.IO) {
            println("Insert method called with data: $member") // 로그 추가
            memberRepository.save(member)
        }

        return RespData.of(
            MessageCode.SUCCESS.code,
            "${member.userid}님 환영합니다. 회원가입이 완료되었습니다. 로그인 후 이용해주세요.",
            member
        )
    }

    fun findByUserid(userid: String): Member? {
        val member = memberRepository.findByUserid(userid)
        if (member != null && (member.useYn == null || member.useYn.uppercase() != "Y")) {
            logger.warn("Attempt to retrieve disabled user: $userid")
            return null
        }
        return member
    }

    @Transactional(readOnly = true)
    fun findByUserName(username: String): Member? {
        return memberRepository.findByUsername(username)
            //?: throw UsernameNotFoundException("User not found with username: $username")
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
            MessageCode.SUCCESS.code,  // resultCode에 SUCCESS 코드 추가
            "${member.username}님 안녕하세요.",
            AuthAndMakeTokensResponseBody(member, accessToken, refreshToken) // data에 ResponseBody 추가
        )
    }

    fun validateToken(token: String): Boolean {
        return authTokenService.validateToken(token)
    }

    fun refreshAccessToken(refreshToken: String): RespData<String> {
        // JWT 토큰으로 멤버 조회, null일 경우 예외 발생
        val member = memberRepository.findByJwtToken(refreshToken)
            ?: throw GlobalException(MessageCode.BAD_REQUEST.code, MessageCode.BAD_REQUEST.message)

        // 엑세스 토큰 생성
        val accessToken = authTokenService.genAccessToken(member)

        // 응답 데이터 반환
        return RespData.of(MessageCode.SUCCESS.code, MessageCode.SUCCESS.message, accessToken)
    }

    fun getUserFromAccessToken(accessToken: String): SecurityUser {
        val payloadBody = authTokenService.getDataFrom(accessToken)

        val id = (payloadBody["id"] as Int).toLong() // id는 Long으로 변환
        val username = payloadBody["username"] as String
        val authorities = payloadBody["authorities"] as List<String>

        return SecurityUser(
            id,
            username,
            "",
            authorities.map { SimpleGrantedAuthority(it) } // SimpleGrantedAuthority 객체 생성
        )
    }
    @Transactional
    suspend fun modifyOrJoin(
        username: String,
        nickname: String,
        providerTypeCode: String,
        imageBytes: ByteArray,
        profileImgUrl: String,
        userid: String,
        userEmail: String,
    ): RespData<Member> {
        logger.debug("modifyOrJoin called with username=$username, userid=$userid")

        val finalUserid = if (userid.isBlank()) username else userid
        val existingMember = findByUserid(finalUserid)
        val finalUserEmail = if (userEmail.isBlank()) "guest@localhost.co.kr" else userEmail  // 삼항 연산자 대체

        return if (existingMember == null) {
            // 새 계정 생성
            join(finalUserid, username,nickname,  finalUserEmail,
                passwordEncoder.encode(username), "image/jpeg", imageBytes,
                M_Role.MEMBER.authority, profileImgUrl, providerTypeCode)
        } else {
            // 기존 계정 업데이트
            logger.info("Updating existing member: $finalUserid")
            modify(existingMember, nickname, profileImgUrl)
        }
    }


    @Transactional
    fun modify(member: Member, nickname: String, profileImgUrl: String): RespData<Member> {
        member.nickname = nickname
        member.profileImgUrl = profileImgUrl
        logger.debug("Modifying member with id=${member.id}")
        // RespData.of를 사용하여 반환
        return RespData.of(MessageCode.SUCCESS.code, "회원정보가 수정되었습니다: ${member.username ?: "Unknown User"}", member)
    }

    fun getMemberByAuthentication(authentication: Authentication): Member? {
        val username = authentication.name
        return findByUserName(username)
    }

    @Transactional
    fun updateMemberJwtToken(memberId: Long, newToken: String) {
        val member = memberRepository.findById(memberId).orElseThrow { IllegalArgumentException("Member not found") }
        member.jwtToken = newToken
        memberRepository.save(member)
    }

    @Transactional(readOnly = true)
    fun searchMembersByUsername(searchUserName: String): List<Member> {
        return memberRepository.findByUsernameContaining(searchUserName) // 키워드를 포함하는 사용자 조회
    }

    fun getCurrentUser(): Member {
        // Implement the logic to get the current authenticated user
        // This is just a placeholder implementation
        return memberRepository.findAll().firstOrNull()
            ?: throw IllegalStateException("No users found in the system")
    }

    // 사용자 이름과 이메일, accountType으로 멤버 찾기
    fun findMember(username: String, userEmail: String, accountType: String): Optional<Member> {
        return memberRepository.findByUsernameAndUserEmailAndAccountType(username, userEmail, accountType)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun update(id: Long, updateData: MemberUpdateData, imageBytes: ByteArray?, imageType: String?): Member? {
        return TransactionTemplate(transactionManager).execute { _ ->
            val member = memberRepository.findById(id).orElseThrow { IllegalArgumentException("Member not found with id: $id") }

            updateData.nickname?.let { member.nickname = it }
            updateData.userEmail?.let { member.userEmail = it }
            updateData.password?.let { member.password = passwordEncoder.encode(it) }
            updateData.useYn?.let { member.useYn = it }
            updateData.roleType?.let { member.roleType = it }
            updateData.accountType?.let { member.accountType = it }
            updateData.jwtToken?.let { member.jwtToken = it }

            if (imageBytes != null && imageType != null) {
                member.image = imageBytes
                member.imageType = imageType
            }

            val updatedMember = memberRepository.save(member)
            entityManager.flush()
            updatedMember
        }
    }

}