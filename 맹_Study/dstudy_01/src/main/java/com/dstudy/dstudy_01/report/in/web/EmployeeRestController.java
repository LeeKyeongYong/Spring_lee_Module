package com.dstudy.dstudy_01.report.in.web;

import com.dstudy.dstudy_01.report.in.GetEmployeeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeRestController {
    private final GetEmployeeUseCase getEmployeeUseCase;

    @GetMapping
    public ResponseEntity<?> getEmployees(@RequestParam(required = false) String type,
                                          HttpServletResponse response) {
        if ("xls".equals(type)) {
            getEmployeeUseCase.generateExcelReport();
            return ResponseEntity.ok().build();
        } else if ("pdf".equals(type)) {
            getEmployeeUseCase.generatePdfReport();
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok(getEmployeeUseCase.getAllEmployees());
    }
}
