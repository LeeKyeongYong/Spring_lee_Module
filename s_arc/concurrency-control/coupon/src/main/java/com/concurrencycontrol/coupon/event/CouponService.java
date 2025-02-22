package com.concurrencycontrol.coupon.event;

import java.util.Random;

import com.concurrencycontrol.coupon.annotation.DistributedLock;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 쿠폰 발급과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 이벤트에 따라 쿠폰을 발급하고, 쿠폰 발급 수량을 관리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final EventRepository eventRepository;

    /**
     * 이벤트 ID를 통해 쿠폰을 발급하는 메서드입니다.
     * DistributedLock 어노테이션을 통해 분산 락을 적용하여 동시성 문제를 해결합니다.
     * @param eventId 이벤트 ID
     * @return 발급된 쿠폰 객체
     */
    @DistributedLock(key = "eventId", waitTime = 20L, leaseTime = 7L)
    public Coupon issue(final Long eventId) {

        // 이벤트 정보를 데이터베이스에서 조회합니다.
        Event event = eventRepository.findById(eventId)
                .orElseThrow();  // 이벤트가 없으면 예외를 던집니다.

        // 새로운 회원 ID를 생성합니다.
        Long memberId = new Random().nextLong();

        // 해당 회원이 이미 쿠폰을 발급받았는지 확인합니다.
        if (couponRepository.findByMemberId(memberId).isPresent()) {
            throw new EntityExistsException("이미 발급된 쿠폰이 존재합니다.");
        }

        // 쿠폰을 발급하고 저장합니다.
        Coupon coupon = couponRepository.save(Coupon.from(event.getId(), memberId));

        // 이벤트의 쿠폰 수량을 감소시킵니다.
        event.decreaseCouponQuantity();

        // 쿠폰 발급 정보를 로그로 기록합니다.
        log.info("[COUPON ISSUED] COUPON ID : {}, MEMBER ID : {}", coupon.getId(), coupon.getMemberId());

        // 발급된 쿠폰을 반환합니다.
        return coupon;
    }
}