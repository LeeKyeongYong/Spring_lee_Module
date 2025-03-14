package com.dstudy.dstudy_01.report.application;

import com.dstudy.dstudy_01.report.domain.Employee;
import com.dstudy.dstudy_01.report.in.GetEmployeeUseCase;
import com.dstudy.dstudy_01.report.out.LoadEmployeePort;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
class EmployeeService implements GetEmployeeUseCase {
    private final LoadEmployeePort loadEmployeePort;
    private final ExcelReportGenerator excelReportGenerator;
    private final PdfReportGenerator pdfReportGenerator;

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return loadEmployeePort.loadAllEmployees();
    }

    @Override
    public void generateExcelReport(HttpServletResponse response) {
        List<Employee> employees = getAllEmployees();
        excelReportGenerator.generate(employees, response);
    }

    @Override
    public void generatePdfReport(HttpServletResponse response) {
        List<Employee> employees = getAllEmployees();
        pdfReportGenerator.generate(employees,response);
    }
}