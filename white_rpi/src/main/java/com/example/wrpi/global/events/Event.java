package com.example.wrpi.global.events;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Integer id;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus = EventStatus.DRAFT;

    private String name; //이벤트 이름
    private String description; //이벤트 설명
    private LocalDateTime beginEnrollmentDateTime; //이벤트 등록시작시간
    private LocalDateTime closeEnrollmentDateTime; //이벤트 종료시간
    private LocalDateTime beginEventDateTime; //이벤트 시작일시
    private LocalDateTime endEventDateTime; //이벤트 종료일시
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional) 참가비 기본
    private int maxPrice; // (optional)  참가비 최대 (*) maxprice가 0이고 basePrice가 값이 존재하면 무한 경매가 됨
    private int limitOfEnrollment; //등록제한

}
