package com.concurrencycontrol.coupon.event;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * 쿠폰 발급 API를 제공하는 REST 컨트롤러입니다.
 * 쿠폰 발급 요청을 받아서 쿠폰을 발급하고 결과를 반환합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    /**
     * 이벤트 ID에 대한 쿠폰을 발급하는 엔드포인트입니다.
     * @param eventId 이벤트 ID
     * @return 발급된 쿠폰 객체
     */
    @PostMapping("events/{eventId}")
    public Coupon issue(@PathVariable Long eventId) {

        // 쿠폰 발급 서비스를 호출하여 쿠폰을 발급하고 반환합니다.
        return couponService.issue(eventId);
    }
}