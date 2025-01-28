package com.dstudy.dstudy_01.subject;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SubjectInfo {
    private Integer hours;
    private LocalDate openDate;
    private Long id;
    private String name;

}
