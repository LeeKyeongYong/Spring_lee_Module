package com.krstudy.kapi.domain.member.datas

import com.krstudy.kapi.domain.oauth2.entity.Social
import com.krstudy.kapi.domain.member.service.MemberService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentLinkedQueue

@Component
class RegistrationQueue(private val memberService: MemberService) {
    private val queue = ConcurrentLinkedQueue<RegistrationData>()
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            while (true) {
                queue.poll()?.let { registrationData ->
                    val (userid, username, password, userEmail, imageType, imageBytes, social) = registrationData

                    val finalImageBytes = imageBytes ?: getDefaultImageBytes()
                    // roleType의 기본값 설정
                    val roleType = "ROLE_MEMBER"

                    // social이 null이면 WEB으로 설정
                    val finalSocial = social ?: Social.WEB

                    memberService.join(
                        userid,
                        username,
                        userEmail,
                        password,
                        imageType ?: "image/jpeg",
                        finalImageBytes,
                        roleType,
                        finalSocial // null이 아닐 경우 해당 값을, null이면 Social.WEB 저장
                    )
                }
            }
        }
    }

    fun enqueue(userid: String, username: String, password: String, userEmail: String, imageType: String? = null, imageBytes: ByteArray? = null, social: Social? = null) {
        queue.add(RegistrationData(userid, username, password, userEmail, imageType, imageBytes, social))
    }

    private fun getDefaultImageBytes(): ByteArray {
        // 기본 이미지 파일을 불러와서 ByteArray로 변환하는 로직
        return byteArrayOf() // 실제로 반환할 기본 이미지 바이트 배열 사용
    }
}