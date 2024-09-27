package com.jqstudy.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq_generator")
    @SequenceGenerator(name = "employee_seq_generator", sequenceName = "EMPLOYEE_SEQUENCE", allocationSize = 1)
    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hire_date")
    private LocalDate hireDate; // LocalDate로 변경

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "commission_pct")
    private Double commissionPct;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "department_id")
    private Integer departmentId;

    public Employee() {}

    public Employee(Integer employeeId, String firstName, String lastName,
                    String email, String phoneNumber, LocalDate hireDate, String jobId,
                    Double salary, Double commissionPct, Integer managerId,
                    Integer departmentId) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.hireDate = hireDate;
        this.jobId = jobId;
        this.salary = salary;
        this.commissionPct = commissionPct;
        this.managerId = managerId;
        this.departmentId = departmentId;
    }
}
