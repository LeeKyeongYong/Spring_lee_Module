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
    // Triple 사용으로 변경
    private val queue = ConcurrentLinkedQueue<Triple<String, String, String>>()
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            while (true) {
                val triple = queue.poll()
                if (triple != null) {
                    val (userid, username, password) = triple
                    memberService.join(userid, username, password, "")
                }
            }
        }
    }

    // 세 값을 받아서 Triple로 큐에 추가
    fun enqueue(userid: String, username: String, password: String) {
        queue.add(Triple(userid, username, password))
    }
}
