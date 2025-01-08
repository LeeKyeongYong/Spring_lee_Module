package com.dstudy.dstudy_01.report.out.persistence;

import com.dstudy.dstudy_01.report.domain.Employee;
import com.dstudy.dstudy_01.report.domain.EmployeeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee mapToDomainEntity(EmployeeJpaEntity entity) {
        return new Employee(
                entity.getEmployeeId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhoneNumber(),
                entity.getHireDate(),
                entity.getJobId(),
                entity.getSalary(),
                entity.getCommissionPct(),
                entity.getManagerId(),
                entity.getDepartmentId()
        );
    }

    public EmployeeJpaEntity mapToJpaEntity(Employee employee) {
        return new EmployeeJpaEntity(
                employee.getEmployeeId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getHireDate(),
                employee.getJobId(),
                employee.getSalary(),
                employee.getCommissionPct(),
                employee.getManagerId(),
                employee.getDepartmentId()
        );
    }
}