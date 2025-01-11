package com.dstudy.dstudy_01.report.in.web;

import com.dstudy.dstudy_01.global.https.ReqData;
import com.dstudy.dstudy_01.report.domain.Employee;
import com.dstudy.dstudy_01.report.in.GetEmployeeUseCase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeWebController {
    private final GetEmployeeUseCase getEmployeeUseCase;
    private final ReqData rq;

    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = getEmployeeUseCase.getAllEmployees();
        rq.setAttribute("employees", employees);
        return "domain/employee/list";
    }

    @PostMapping("/excel")
    public String generateExcelReport(HttpServletResponse response) {
        getEmployeeUseCase.generateExcelReport(response);
        return "redirect:/employees";
    }

    @PostMapping("/pdf")
    public String generatePdfReport(HttpServletResponse response) {
        getEmployeeUseCase.generatePdfReport(response);
        return "redirect:/employees";
    }
}