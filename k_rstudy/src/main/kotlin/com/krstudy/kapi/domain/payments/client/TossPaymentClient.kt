package com.krstudy.kapi.domain.payments.client

import com.krstudy.kapi.domain.payments.dto.TossPaymentResult
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class TossPaymentClient {
    fun confirmPayment(paymentKey: String, orderId: String, amount: BigDecimal): TossPaymentResult {
        // 실제 토스페이먼츠 API 호출 로직 구현
        // 임시로 성공 결과 반환
        return TossPaymentResult(
            customerKey = "customer123",
            customerName = "김토스",
            customerEmail = "customer123@gmail.com"
        )
    }
}