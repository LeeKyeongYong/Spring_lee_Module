package com.jqstudy.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class EmployeesForDTO {

    private int total;   // 전체 페이지 수
    private int page;    // 표시할 페이지 수
    private int records; // 전체 레코드 갯수
    private List<Employee> rows = new ArrayList<>();
    public EmployeesForDTO() {}
    public EmployeesForDTO(int total, int page, int records,
                           List<Employee> employees) {
        this.total = total;
        this.page = page;
        this.records = records;
        this.rows = employees;
    }
}