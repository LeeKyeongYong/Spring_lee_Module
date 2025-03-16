package com.study.aop008.repository;

import com.study.aop008.domain.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Customer customer) {
        final String sql = "INSERT INTO customer(name, email, phone, birth_date) VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setObject(4, customer.getBirthDate());
            return ps;
        }, keyHolder);

        customer.setId(keyHolder.getKey().intValue());
    }

    @Override
    public List<Customer> findAll() {
        final String sql = "SELECT id, name, email, phone, birth_date FROM customer ORDER BY id";

        RowMapper<Customer> rowMapper = (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setName(rs.getString("name"));
            customer.setEmail(rs.getString("email"));
            customer.setPhone(rs.getString("phone"));
            customer.setBirthDate(rs.getObject("birth_date", LocalDate.class));
            return customer;
        };

        return jdbcTemplate.query(sql, rowMapper);
    }
}