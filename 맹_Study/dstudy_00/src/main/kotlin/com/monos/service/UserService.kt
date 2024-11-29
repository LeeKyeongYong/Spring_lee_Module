package com.monos.service

import com.monos.dto.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
@Service
class UserService {

    // Flow를 통해 사용자 데이터를 변환
    fun processUsers(userList: Flow<User>): Flow<User> {
        return userList.map { user ->
            user.copy(name = user.name.uppercase()) // 이름을 대문자로 변환
        }
    }

    // 스트리밍 데이터 생성
    fun createUserStream(): Flow<String> {
        return flow {
            for (i in 1..10) {
                kotlinx.coroutines.delay(1000) // 1초 대기
                emit("Hello from duplex communication! $i")
            }
        }
    }
}