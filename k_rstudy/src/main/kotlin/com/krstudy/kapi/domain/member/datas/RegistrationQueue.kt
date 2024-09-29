package com.krstudy.kapi.domain.member.datas

import com.krstudy.kapi.domain.member.service.MemberService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentLinkedQueue

@Component
class RegistrationQueue(
    private val memberService: MemberService
) {
    private val queue = ConcurrentLinkedQueue<RegistrationData>()
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            while (true) {
                val registrationData = queue.poll()
                if (registrationData != null) {
                    val (userid, username, password, userEmail, imageType, imageBytes, additionalFields) = registrationData

                    // 파일이 없는 경우 기본 이미지 설정
                    val finalImageType = imageType ?: "defaultImageType" // 기본 이미지 타입
                    val finalImageBytes = imageBytes ?: getDefaultImageBytes() // 기본 이미지 바이트

                    memberService.join(userid, username, userEmail, password, finalImageType, finalImageBytes)
                }
            }
        }
    }

    // 데이터를 추가할 때 추가 필드를 Map으로 전달 가능
    fun enqueue(userid: String, username: String, password: String, userEmail: String, imageType: String? = null, imageBytes: ByteArray? = null) {
        queue.add(RegistrationData(userid, username, password, userEmail, imageType, imageBytes))
    }

    private fun getDefaultImageBytes(): ByteArray {
        // 기본 이미지 파일을 불러와서 ByteArray로 변환하는 로직
        return byteArrayOf() // 실제로 반환할 기본 이미지 바이트 배열을 넣어주세요
    }
}