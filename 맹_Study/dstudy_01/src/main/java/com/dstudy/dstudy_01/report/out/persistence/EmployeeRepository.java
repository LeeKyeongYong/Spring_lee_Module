package com.dstudy.dstudy_01.report.out.persistence;

import com.dstudy.dstudy_01.report.domain.EmployeeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeJpaEntity, Long> {
}