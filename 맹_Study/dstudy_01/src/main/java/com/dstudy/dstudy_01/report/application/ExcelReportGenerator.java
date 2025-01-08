package com.dstudy.dstudy_01.report.application;

import com.dstudy.dstudy_01.report.domain.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelReportGenerator {
    public void generate(List<Employee> employees) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Employees");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Employee ID");
            headerRow.createCell(1).setCellValue("First Name");
            headerRow.createCell(2).setCellValue("Last Name");
            headerRow.createCell(3).setCellValue("Email");
            headerRow.createCell(4).setCellValue("Phone Number");
            headerRow.createCell(5).setCellValue("Hire Date");
            headerRow.createCell(6).setCellValue("Job ID");
            headerRow.createCell(7).setCellValue("Salary");
            headerRow.createCell(8).setCellValue("Commission");
            headerRow.createCell(9).setCellValue("Manager ID");
            headerRow.createCell(10).setCellValue("Department ID");

            // Create data rows
            int rowNum = 1;
            for (Employee employee : employees) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(employee.getEmployeeId());
                row.createCell(1).setCellValue(employee.getFirstName());
                row.createCell(2).setCellValue(employee.getLastName());
                row.createCell(3).setCellValue(employee.getEmail());
                row.createCell(4).setCellValue(employee.getPhoneNumber());
                row.createCell(5).setCellValue(employee.getHireDate());
                row.createCell(6).setCellValue(employee.getJobId());
                row.createCell(7).setCellValue(employee.getSalary());
                row.createCell(8).setCellValue(employee.getCommissionPct());
                row.createCell(9).setCellValue(employee.getManagerId());
                row.createCell(10).setCellValue(employee.getDepartmentId());
            }

            // Auto size columns
            for (int i = 0; i < 11; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream("employees.xlsx")) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }
}