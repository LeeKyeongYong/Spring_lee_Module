package com.jqstudy.domain.entity;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;

    public class EmployeeFactoryDto {

        private static final Logger logger = LoggerFactory.getLogger(EmployeeFactoryDto.class);
        private static final String DATE_FORMAT = "yyyy/MM/dd";

        public static Employee createEmployeeFromDto(EmployeesDto employeeDto) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
            Employee employee = new Employee();

            employee.setFirstName(employeeDto.getFirstName());
            employee.setLastName(employeeDto.getLastName());
            employee.setEmail(employeeDto.getEmail());
            employee.setPhoneNumber(employeeDto.getPhoneNumber());
            employee.setJobId(employeeDto.getJobId());
            employee.setSalary(employeeDto.getSalary());
            employee.setCommissionPct(employeeDto.getCommissionPct());
            employee.setManagerId(employeeDto.getManagerId());
            employee.setDepartmentId(employeeDto.getDepartmentId());

            if (isValidOperation(employeeDto.getOper())) {
                String hireDate = employeeDto.getHireDate();
                employee.setHireDate(parseHireDate(hireDate, dtf));
            }

            return employee;
        }

        private static boolean isValidOperation(String operation) {
            return Set.of("add", "edit").contains(operation);
        }

        private static LocalDate parseHireDate(String hireDate, DateTimeFormatter dtf) {
            try {
                return LocalDate.parse(hireDate, dtf);
            } catch (DateTimeParseException e) {
                logger.info("Date parsing error for hireDate: " + hireDate, e);
                throw new IllegalArgumentException("Invalid date format. Use " + DATE_FORMAT + ".");
            }
        }
    }
