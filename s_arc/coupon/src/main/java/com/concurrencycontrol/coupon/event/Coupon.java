package com.concurrencycontrol.coupon.event;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 쿠폰 정보를 저장하는 엔티티 클래스입니다.
 * 쿠폰은 고유한 ID를 가지며, 이벤트 ID와 회원 ID의 조합이 유니크해야 합니다.
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints =
@UniqueConstraint(columnNames = {"eventId", "memberId"}))  // 이벤트 ID와 회원 ID가 유니크한 조합이어야 함
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventId;  // 발급된 이벤트 ID

    private Long memberId;  // 쿠폰을 받은 회원 ID

    /**
     * Coupon 객체를 생성하는 정적 팩토리 메서드입니다.
     * @param eventId 이벤트 ID
     * @param memberId 회원 ID
     * @return 생성된 Coupon 객체
     */
    public static Coupon from(Long eventId, Long memberId) {

        return new Coupon(eventId, memberId);
    }

    /**
     * 생성자를 private로 숨기고, 정적 팩토리 메서드를 통해 객체를 생성합니다.
     * @param eventId 이벤트 ID
     * @param memberId 회원 ID
     */
    protected Coupon(Long eventId, Long memberId) {

        this.eventId = eventId;
        this.memberId = memberId;
    }
}