package com.dstudy.dstudy_01.report.out.persistence;

import com.dstudy.dstudy_01.report.domain.Employee;
import com.dstudy.dstudy_01.report.out.LoadEmployeePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
class EmployeePersistenceAdapter implements LoadEmployeePort {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<Employee> loadAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::mapToDomainEntity)
                .collect(Collectors.toList());
    }
}