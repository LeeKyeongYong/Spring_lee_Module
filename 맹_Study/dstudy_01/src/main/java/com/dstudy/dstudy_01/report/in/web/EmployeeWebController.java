package com.dstudy.dstudy_01.report.in.web;

import com.dstudy.dstudy_01.report.domain.Employee;
import com.dstudy.dstudy_01.report.in.GetEmployeeUseCase;
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

    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = getEmployeeUseCase.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee/list";
    }

    @PostMapping("/excel")
    public String generateExcelReport() {
        getEmployeeUseCase.generateExcelReport();
        return "redirect:/employees";
    }

    @PostMapping("/pdf")
    public String generatePdfReport() {
        getEmployeeUseCase.generatePdfReport();
        return "redirect:/employees";
    }
}