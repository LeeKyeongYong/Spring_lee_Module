package com.concurrencycontrol.coupon.event;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Event 엔티티에 대한 데이터베이스 접근을 처리하는 JPA 레포지토리입니다.
 * JpaRepository를 확장하여 기본적인 CRUD 기능을 제공합니다.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}