package com.dstudy.dstudy_01.report.in;

import com.dstudy.dstudy_01.report.domain.Employee;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface GetEmployeeUseCase {
    List<Employee> getAllEmployees();
    void generateExcelReport(HttpServletResponse response);
    void generatePdfReport(HttpServletResponse response);
}