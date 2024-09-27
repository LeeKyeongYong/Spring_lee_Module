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

        // 페이지 값이 0 미만인 경우 처리
        if (page < 1) {
            page = 1;  // 1페이지를 기본으로 설정
        }

        // 페이지 요청을 0으로 맞추기 위해 1을 빼줌
        int pageIndex = page - 1;

        Specification<Employee> spec = (root, query, criteriaBuilder) -> {
            if (_search) {
                return criteriaBuilder.like(root.get(searchField), "%" + searchString + "%");
            }
            return criteriaBuilder.conjunction();
        };

        return employeeRepository.findAll(spec, PageRequest.of(pageIndex, rows, Sort.by(Sort.Direction.fromString(sord), sidx))).getContent();
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