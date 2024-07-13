package coroutines

import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch { // 새로운 코루틴을 백그라운드에서 실행
        delay(1000L) // 1초의 비동기적인 대기(쓰레드를 차단하지 않음)
        println("World!") // 대기 후 출력
    }
    println("Hello,") // 코루틴이 대기 중일 때 출력
    runBlocking {     // 메인 스레드가 코루틴이 끝날 때까지 대기
        delay(2000L)
    }
}