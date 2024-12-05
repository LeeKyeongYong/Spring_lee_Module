package com.krstudy.kapi.domain.payments.event

import com.krstudy.kapi.domain.payments.entity.Payment
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class PaymentEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    fun publishPaymentCompletedEvent(payment: Payment) {
        applicationEventPublisher.publishEvent(
            PaymentEvent.PaymentCompleted(
                paymentId = payment.id,
                orderId = payment.orderId,
                amount = payment.amount,
                memberUserId = payment.member.userid
            )
        )
    }
}