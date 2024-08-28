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
                    val (userid, username, password, userEmail, additionalFields) = registrationData
                    // 여기서 추가 필드도 사용할 수 있음
                    memberService.join(userid, username, password, userEmail, "")
                }
            }
        }
    }

    // 데이터를 추가할 때 추가 필드를 Map으로 전달 가능
    fun enqueue(userid: String, username: String, password: String, userEmail: String, additionalFields: Map<String, Any> = emptyMap()) {
        queue.add(RegistrationData(userid, username, password, userEmail, additionalFields))
    }
}