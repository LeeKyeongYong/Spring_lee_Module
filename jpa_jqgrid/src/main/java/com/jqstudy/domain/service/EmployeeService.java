package com.jqstudy.domain.service;

import com.jqstudy.domain.entity.Employee;

import java.util.List;

public interface EmployeeService {
    int getTotalRecord(boolean _search, String searchField, String searchString, String searchOper);
    List<Employee> selectEmployees(boolean _search, int page, int rows, String sidx, String sord,
                                   String searchField, String searchString, String searchOper);
    void insertEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(int employeeId);
}