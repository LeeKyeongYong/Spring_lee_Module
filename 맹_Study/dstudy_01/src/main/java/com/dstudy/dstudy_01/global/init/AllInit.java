package com.dstudy.dstudy_01.global.init;

import com.dstudy.dstudy_01.report.domain.EmployeeJpaEntity;
import com.dstudy.dstudy_01.report.out.persistence.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class AllInit {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
        return args -> {
            //List<EmployeeJpaEntity> employees = generateEmployees(1000000);
            List<EmployeeJpaEntity> employees = generateEmployees(10);
            employeeRepository.saveAll(employees);
        };
    }

    private List<EmployeeJpaEntity> generateEmployees(int count) {
        List<EmployeeJpaEntity> employees = new ArrayList<>();
        Random random = new Random();

        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emma"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis"};
        String[] jobIds = {"IT_PROG", "SA_REP", "FI_ACCOUNT", "FI_MGR", "PU_CLERK", "ST_CLERK"};

        for (int i = 1; i <= count; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String email = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 4).toUpperCase() + i + "@company.com";
            LocalDate localHireDate = LocalDate.now().minusDays(random.nextInt(365 * 5));
            Date hireDate = java.sql.Date.valueOf(localHireDate);

            EmployeeJpaEntity employee = new EmployeeJpaEntity(
                    (long) i,
                    firstName,
                    lastName,
                    email,
                    String.format("010-%04d-%04d", random.nextInt(10000), random.nextInt(10000)),
                    hireDate,
                    jobIds[random.nextInt(jobIds.length)],
                    (double)(3000 + random.nextInt(7000)),
                    (double)(random.nextFloat() * 0.4f),
                    random.nextInt(5) + 1L,
                    random.nextInt(11) + 10L
            );

            employees.add(employee);
        }

        return employees;
    }
}
