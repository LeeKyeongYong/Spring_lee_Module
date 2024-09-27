package com.jqstudy.domain.controller;

import com.jqstudy.domain.entity.Employee;
import com.jqstudy.domain.entity.EmployeesDto;
import com.jqstudy.domain.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import com.jqstudy.domain.entity.EmployeeFactoryDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api")
public class V1EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(V1EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/read")
    public ResponseEntity<EmployeesDto> read(
            @RequestParam(value = "_search", defaultValue = "false") Boolean _search,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "rows") Integer rows,
            @RequestParam(value = "sidx", defaultValue = "id") String sidx,
            @RequestParam(value = "sord", defaultValue = "asc") String sord,
            @RequestParam(value = "searchField", required = false) String searchField,
            @RequestParam(value = "searchString", required = false) String searchString,
            @RequestParam(value = "searchOper", required = false) String searchOper) throws IOException {

        page = Math.max(0, page);
        rows = Math.max(1, rows);

        int records = employeeService.getTotalRecord(_search, searchField, searchString, searchOper);
        int total = (records > 0) ? (int) Math.ceil(records / (double) rows) : 0;

        if (total > 0 && page >= total) {
            page = total - 1;
        }

        List<Employee> employees = employeeService.selectEmployees(_search, page, rows, sidx, sord, searchField, searchString, searchOper);
        EmployeesDto dto = new EmployeesDto(total, page, records, Optional.ofNullable(employees).orElse(new ArrayList<>()));
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/edit")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeesDto employeeDto) {
        try {
            Employee employee = EmployeeFactoryDto.createEmployeeFromDto(employeeDto);
            return handleOperation(employeeDto, employee);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing employee", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    private ResponseEntity<String> handleOperation(EmployeesDto employeeDto, Employee employee) {
        var oper = employeeDto.getOper();

        return switch (oper) {
            case "add" -> {
                employeeService.insertEmployee(employee);
                yield ResponseEntity.ok("Employee added successfully");
            }
            case "edit" -> {
                employee.setEmployeeId(Integer.parseInt(employeeDto.getEmployeeId()));
                employeeService.updateEmployee(employee);
                yield ResponseEntity.ok("Employee updated successfully");
            }
            case "del" -> {
                employeeService.deleteEmployee(Integer.parseInt(employeeDto.getEmployeeId()));
                yield ResponseEntity.ok("Employee deleted successfully");
            }
            default -> ResponseEntity.badRequest().body("Invalid operation");
        };
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleJsonParsingException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body("Invalid JSON format: " + e.getMessage());
    }
}