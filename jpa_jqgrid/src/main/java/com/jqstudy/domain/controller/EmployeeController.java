package com.jqstudy.domain.controller;

import com.jqstudy.domain.entity.Employee;
import com.jqstudy.domain.entity.EmployeeDto;
import com.jqstudy.domain.entity.EmployeesForDTO;
import com.jqstudy.domain.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping({"/", "/index"})
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Employee Management API");
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees(
            @RequestParam(name = "_search", defaultValue = "false") boolean search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int rows,
            @RequestParam(defaultValue = "lastName") String sidx,
            @RequestParam(defaultValue = "asc") String sord,
            @RequestParam(required = false) String searchField,
            @RequestParam(required = false) String searchString,
            @RequestParam(required = false) String searchOper) {
        try {
            List<Employee> employees = employeeService.selectEmployees(search, page, rows, sidx, sord, searchField, searchString, searchOper);
            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            // 예외 발생 시, 로그를 기록하고 에러 응답 반환
            e.printStackTrace(); // 로깅 필요 시
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/read")
    public ResponseEntity<EmployeesForDTO> read(
            @RequestParam(value = "_search", defaultValue = "false") Boolean _search,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "rows") Integer rows,
            @RequestParam(value = "sidx", defaultValue = "id") String sidx,
            @RequestParam(value = "sord", defaultValue = "asc") String sord,
            @RequestParam(value = "searchField", required = false) String searchField,
            @RequestParam(value = "searchString", required = false) String searchString,
            @RequestParam(value = "searchOper", required = false) String searchOper) throws IOException {

        // 페이지 인덱스와 rows가 0 이상이 되도록 보장
        page = Math.max(0, page);
        rows = Math.max(1, rows);


        int records = employeeService.getTotalRecord(_search, searchField, searchString, searchOper);
        int total = (records > 0) ? (int) Math.ceil(records / (double) rows) : 0;

        // 요청된 페이지가 전체 페이지 수보다 클 경우 마지막 페이지로 설정
        if (total > 0 && page >= total) {
            page = total - 1; // 0 기반 인덱스 조정
        }

       List<Employee> employees = employeeService.selectEmployees(_search, page, rows, sidx, sord, searchField, searchString, searchOper);
        EmployeesForDTO dto = new EmployeesForDTO();
        dto.setTotal(total);
        dto.setPage(page);
        dto.setRecords(records);
        dto.setRows(employees != null ? employees : new ArrayList<>());

        return ResponseEntity.ok(dto);
    }
    @PostMapping("/edit")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeDto employeeDto) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        System.out.println("Received DTO: " + employeeDto);

        try {
            Employee employee = new Employee();
            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());
            employee.setEmail(employeeDto.getEmail());
            employee.setPhoneNumber(employeeDto.getPhoneNumber());

            // 날짜 파싱 에러 처리
            if (employeeDto.getOper().equals("add") || employeeDto.getOper().equals("edit")) {
                if (employeeDto.getHireDate() != null) {
                    try {
                        employee.setHireDate(LocalDate.parse(employeeDto.getHireDate(), dtf).atStartOfDay());
                    } catch (DateTimeParseException e) {
                        return ResponseEntity.badRequest().body("Invalid date format. Use yyyy/MM/dd.");
                    }
                } else {
                    return ResponseEntity.badRequest().body("Hire date cannot be null for add/edit operations.");
                }
            }

            employee.setJobId(employeeDto.getJobId());
            employee.setSalary(employeeDto.getSalary());
            employee.setCommissionPct(employeeDto.getCommissionPct());
            employee.setManagerId(employeeDto.getManagerId());
            employee.setDepartmentId(employeeDto.getDepartmentId());

            System.out.println("employee: " + employee);

            switch (employeeDto.getOper()) {
                case "add":
                    employeeService.insertEmployee(employee);
                    return ResponseEntity.ok("Employee added successfully");
                case "edit":
                    employee.setEmployeeId(Integer.parseInt(employeeDto.getEmployeeId()));
                    employeeService.updateEmployee(employee);
                    return ResponseEntity.ok("Employee updated successfully");
                case "del":
                    // employeeId 검증 및 삭제
                    if (employeeDto.getEmployeeId() != null) {
                        employeeService.deleteEmployee(Integer.parseInt(employeeDto.getEmployeeId()));
                        System.out.println("Integer.parseInt(employeeDto.getEmployeeId()): " + Integer.parseInt(employeeDto.getEmployeeId()));
                        return ResponseEntity.ok("Employee deleted successfully");
                    } else {
                        return ResponseEntity.badRequest().body("Employee ID cannot be null for deletion.");
                    }
                default:
                    return ResponseEntity.badRequest().body("Invalid operation");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParsingException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
    }

}