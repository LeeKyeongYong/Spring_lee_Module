package com.jqstudy.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDto {
    private String oper;
    private String employeeId; // 선택적, "add"일 때는 필요 없음
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String hireDate;
    //@JsonFormat(pattern = "yyyy/MM/dd")
    //private LocalDate hireDate; // LocalDate로 변경
    private String jobId;
    private Double salary;
    private Double commissionPct;
    private Integer managerId;
    private Integer departmentId;

    // Getters and Setters
}