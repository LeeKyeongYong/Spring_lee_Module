package com.example.wrpi.domain.Events.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    @NotEmpty
    private String name; //이벤트 이름
    @NotEmpty
    private String description; //이벤트 설명
    @NotNull
    private LocalDateTime beginEnrollmentDateTime; //이벤트 등록시작시간
    @NotNull
    private LocalDateTime closeEnrollmentDateTime; //이벤트 종료시간
    @NotNull
    private LocalDateTime beginEventDateTime; //이벤트 시작일시
    @NotNull
    private LocalDateTime endEventDateTime; //이벤트 종료일시
    private String location; // (optional) 이게 없으면 온라인 모임
    @Min(0)
    private int basePrice; // (optional) 참가비 기본
    @Min(0)
    private int maxPrice; // (optional) 참가비 최대 (*) maxprice가 0이고 basePrice가 값이 존재하면 무한 경매가 됨
    @Min(0)
    private int limitOfEnrollment; //등록제한

}
