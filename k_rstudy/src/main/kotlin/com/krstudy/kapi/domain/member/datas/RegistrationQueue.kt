package com.krstudy.kapi.domain.member.datas;

import com.krstudy.kapi.domain.member.service.MemberService;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.launch;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
class RegistrationQueue(
    private val memberService: MemberService
) {
    private val queue = ConcurrentLinkedQueue<Pair<String, String>>()
    private val scope = CoroutineScope(Dispatchers.Default)

    init {
        scope.launch {
            while (true) {
                val pair = queue.poll()
                if (pair != null) {
                    val (username, password) = pair
                    memberService.join(username, password, "")
                }
            }
        }
    }

    fun enqueue(username: String, password: String) {
        queue.add(username to password)
    }
}