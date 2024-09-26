package com.jqstudy.domain.service.impl;

import com.jqstudy.domain.entity.Employee;
import com.jqstudy.domain.repository.EmployeeRepository;
import com.jqstudy.domain.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public int getTotalRecord(boolean _search, String searchField, String searchString, String searchOper) {
        // 동적 쿼리 생성
        Specification<Employee> spec = (root, query, criteriaBuilder) -> {
            if (!_search) {
                return criteriaBuilder.conjunction();
            }
            // 검색 조건 추가
            return criteriaBuilder.like(root.get(searchField), "%" + searchString + "%");
        };
        return (int) employeeRepository.count(spec);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> selectEmployees(boolean _search, int page, int rows, String sidx, String sord,
                                          String searchField, String searchString, String searchOper) {
        Specification<Employee> spec = (root, query, criteriaBuilder) -> {
            if (_search) {
                return criteriaBuilder.like(root.get(searchField), "%" + searchString + "%");
            }
            return criteriaBuilder.conjunction();
        };
        return employeeRepository.findAll(spec, PageRequest.of(page - 1, rows, Sort.by(Sort.Direction.fromString(sord), sidx))).getContent();
    }

    @Override
    public void insertEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(int employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}