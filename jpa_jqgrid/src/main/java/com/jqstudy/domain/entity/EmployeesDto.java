package com.jqstudy.domain.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeesDto {

    private int total;
    private int page;
    private int records;
    private List<Employee> rows = new ArrayList<>();
    private String oper;
    private String employeeId; // 선택적, "add"일 때는 필요 없음
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String jobId;
    private Double salary;
    private Double commissionPct;
    private Integer managerId;
    private Integer departmentId;
    private String hireDate;

    public EmployeesDto() {}

    public EmployeesDto(int total, int page, int records, List<Employee> employees) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.rows = employees;
    }
}