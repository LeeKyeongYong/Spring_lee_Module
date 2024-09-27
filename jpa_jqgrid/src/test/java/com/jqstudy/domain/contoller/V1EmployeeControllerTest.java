package com.jqstudy.domain.contoller;

import com.jqstudy.domain.controller.V1EmployeeController;
import com.jqstudy.domain.entity.Employee;
import com.jqstudy.domain.entity.EmployeesDto;
import com.jqstudy.domain.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class V1EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private V1EmployeeController employeeController;

    public V1EmployeeControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("직원 추가 성공")
    @Test
    void givenEmployeeDto_whenAdd_thenReturnSuccessMessage() {
        // given
        EmployeesDto employeeDto = new EmployeesDto();
        employeeDto.setOper("add");
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
        employeeDto.setPhoneNumber("123-456-7890");
        employeeDto.setHireDate("2023/09/26");
        employeeDto.setJobId("DEV01");
        employeeDto.setSalary(5000.0);
        employeeDto.setCommissionPct(0.1);
        employeeDto.setManagerId(101);
        employeeDto.setDepartmentId(10);

        // when
        doNothing().when(employeeService).insertEmployee(any(Employee.class)); // void 메서드에 대해 doNothing() 사용

        ResponseEntity<String> response = employeeController.addEmployee(employeeDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee added successfully", response.getBody());

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeService).insertEmployee(employeeCaptor.capture());
        assertEquals("John", employeeCaptor.getValue().getFirstName());
    }

    @DisplayName("직원 추가 시 날짜 포맷 오류")
    @Test
    void givenInvalidHireDate_whenAdd_thenReturnErrorMessage() {
        // given
        EmployeesDto employeeDto = new EmployeesDto();
        employeeDto.setOper("add");
        employeeDto.setHireDate("invalid-date");

        // when
        ResponseEntity<String> response = employeeController.addEmployee(employeeDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid date format. Use yyyy/MM/dd.", response.getBody());
    }

    @DisplayName("직원 삭제 성공")
    @Test
    void givenEmployeeId_whenDelete_thenReturnSuccessMessage() {
        // given
        EmployeesDto employeeDto = new EmployeesDto();
        employeeDto.setOper("del");
        employeeDto.setEmployeeId("14");

        // when
        doNothing().when(employeeService).deleteEmployee(14); // void 메서드에 대해 doNothing() 사용
        ResponseEntity<String> response = employeeController.addEmployee(employeeDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());
        verify(employeeService).deleteEmployee(14);
    }

    @DisplayName("직원 삭제 시 ID가 null인 경우")
    @Test
    void givenNullEmployeeId_whenDelete_thenReturnErrorMessage() {
        // given
        EmployeesDto employeeDto = new EmployeesDto();
        employeeDto.setOper("del");

        // when
        ResponseEntity<String> response = employeeController.addEmployee(employeeDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Employee ID cannot be null for deletion.", response.getBody());
    }
}