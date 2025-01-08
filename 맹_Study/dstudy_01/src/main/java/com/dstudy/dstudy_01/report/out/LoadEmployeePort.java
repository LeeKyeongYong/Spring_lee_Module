package com.dstudy.dstudy_01.report.out;

import com.dstudy.dstudy_01.report.domain.Employee;
import java.util.List;

public interface LoadEmployeePort {
    List<Employee> loadAllEmployees();
}