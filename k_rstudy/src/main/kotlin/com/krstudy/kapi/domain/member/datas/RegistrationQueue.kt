package com.krstudy.kapi.domain.member.datas

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
                    val (userid, username, password, userEmail, imageType, imageBytes) = registrationData

                    val finalImageBytes = imageBytes ?: getDefaultImageBytes()
                    memberService.join(userid, username, userEmail, password, imageType ?: "defaultImageType", finalImageBytes, "")
                }
            }
        }
    }

    fun enqueue(userid: String, username: String, password: String, userEmail: String, imageType: String? = null, imageBytes: ByteArray? = null) {
        queue.add(RegistrationData(userid, username, password, userEmail, imageType, imageBytes))
    }

    private fun getDefaultImageBytes(): ByteArray {
        // 기본 이미지 파일을 불러와서 ByteArray로 변환하는 로직
        return byteArrayOf() // 실제로 반환할 기본 이미지 바이트 배열을 넣어주세요
    }
}