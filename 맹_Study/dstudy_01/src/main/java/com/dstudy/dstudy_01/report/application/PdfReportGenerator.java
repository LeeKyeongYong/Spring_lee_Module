package com.dstudy.dstudy_01.report.application;

import com.dstudy.dstudy_01.report.domain.Employee;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.util.List;

@Component
public class PdfReportGenerator {
    public void generate(List<Employee> employees) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("employees.pdf"));
            document.open();

            // Add title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Employee Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Create table
            PdfPTable table = new PdfPTable(11);
            table.addCell("Employee ID");
            table.addCell("First Name");
            table.addCell("Last Name");
            table.addCell("Email");
            table.addCell("Phone Number");
            table.addCell("Hire Date");
            table.addCell("Job ID");
            table.addCell("Salary");
            table.addCell("Commission");
            table.addCell("Manager ID");
            table.addCell("Department ID");

            // Add data rows
            for (Employee employee : employees) {
                table.addCell(String.valueOf(employee.getEmployeeId()));
                table.addCell(employee.getFirstName());
                table.addCell(employee.getLastName());
                table.addCell(employee.getEmail());
                table.addCell(employee.getPhoneNumber());
                table.addCell(employee.getHireDate().toString());
                table.addCell(employee.getJobId());
                table.addCell(String.valueOf(employee.getSalary()));
                table.addCell(String.valueOf(employee.getCommissionPct()));
                table.addCell(String.valueOf(employee.getManagerId()));
                table.addCell(String.valueOf(employee.getDepartmentId()));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }
}