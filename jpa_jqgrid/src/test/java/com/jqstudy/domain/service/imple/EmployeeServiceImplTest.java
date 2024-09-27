package com.jqstudy.domain.service.imple;

import com.jqstudy.domain.entity.Employee;
import com.jqstudy.domain.repository.EmployeeRepository;
import com.jqstudy.domain.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    public EmployeeServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("직원 추가 성공")
    @Test
    void givenEmployee_whenInsert_thenEmployeeSaved() {
        // given
        Employee employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");

        // when
        employeeService.insertEmployee(employee);

        // then
        verify(employeeRepository).save(employee);
    }

    @DisplayName("직원 삭제 성공")
    @Test
    void givenEmployeeId_whenDelete_thenEmployeeRemoved() {
        // given
        int employeeId = 14;

        // when
        employeeService.deleteEmployee(employeeId);

        // then
        verify(employeeRepository).deleteById(employeeId);
    }


    @DisplayName("직원 검색 시 결과 반환")
    @Test
    void givenSearchCriteria_whenSelectEmployees_thenReturnEmployeeList() {
        // given
        List<Employee> expectedEmployees = Collections.singletonList(new Employee());
        Page<Employee> expectedPage = new PageImpl<>(expectedEmployees);
        when(employeeRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(expectedPage);

        // when
        List<Employee> employees = employeeService.selectEmployees(false, 1, 10, "lastName", "asc", null, null, null);

        // then
        assertEquals(1, employees.size());
        verify(employeeRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class));
    }

    @DisplayName("직원 검색 시 결과 반환 실패")
    @Test
    void givenSearchCriteria_whenSelectEmployees_thenReturnEmployeeList2() {
        // given
        List<Employee> expectedEmployees = Collections.singletonList(new Employee());
        when(employeeRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Sort.class)))
                .thenReturn(expectedEmployees);

        // when
        List<Employee> employees = employeeService.selectEmployees(false, 1, 10, "lastName", "asc", null, null, null);

        // then
        assertEquals(1, employees.size());
        verify(employeeRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class), any(org.springframework.data.domain.Sort.class));
    }

    @DisplayName("직원 검색 시 검색 조건 적용")
    @Test
    void givenSearchCriteria_whenGetTotalRecords_thenReturnRecordCount() {
        // given
        when(employeeRepository.count(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(5L);

        // when
        int count = employeeService.getTotalRecord(true, "lastName", "Doe", "eq");

        // then
        assertEquals(5, count);
        verify(employeeRepository).count(any(org.springframework.data.jpa.domain.Specification.class));
    }
}