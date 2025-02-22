package com.concurrencycontrol.coupon.event;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Coupon 엔티티에 대한 데이터베이스 접근을 처리하는 JPA 레포지토리입니다.
 * 쿠폰 발급 관련 데이터 조회 및 저장 작업을 담당합니다.
 */
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * 회원 ID로 쿠폰 정보를 조회하는 메서드입니다.
     * @param memberId 회원 ID
     * @return 쿠폰 정보 (있을 경우)
     */
    Optional<Coupon> findByMemberId(Long memberId);
}