package com.concurrencycontrol.coupon.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 이벤트 정보를 저장하는 엔티티 클래스입니다.
 * 이벤트는 고유한 ID를 가지고, 쿠폰 수량을 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자는 protected로 숨기기
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가하는 ID 설정
    private Long id;

    private String name;  // 이벤트 이름

    private Integer couponQuantity;  // 쿠폰 수량

    /**
     * 쿠폰 수량을 감소시키는 메서드입니다.
     * 쿠폰이 발급될 때마다 호출되어 수량을 줄입니다.
     */
    public void decreaseCouponQuantity() {
        this.couponQuantity--;
    }
}