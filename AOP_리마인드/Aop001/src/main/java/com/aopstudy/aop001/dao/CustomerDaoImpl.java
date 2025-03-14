package com.aopstudy.aop001.dao;

import com.aopstudy.aop001.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


@Repository
public class CustomerDaoImpl implements CustomerDao {

    private final com.aopstudy.aop001.dao.CustomerRepository customerRepository;

    @Autowired
    public CustomerDaoImpl(com.aopstudy.aop001.dao.CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void addCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}

//@Repository
//public class CustomerDaoImpl implements CustomerDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public CustomerDaoImpl(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public void addCustomer(final Customer customer) {
//        final String sql = "INSERT INTO customer(name, email, phone, birth_date) VALUES(?, ?, ?, ?)";
//        KeyHolder holder = new GeneratedKeyHolder();
//
//        jdbcTemplate.update(new PreparedStatementCreator() {
//            @Override
//            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//                pstmt.setString(1, customer.getName());
//                pstmt.setString(2, customer.getEmail());
//                pstmt.setString(3, customer.getPhone());
//                pstmt.setDate(4, new java.sql.Date(customer.getBirthDate().getTime()));
//                return pstmt;
//            }
//        }, holder);
//
//        customer.setNo(holder.getKey().intValue());
//    }
//
//    @Override
//    public List<Customer> getAllCustomers() {
//        final String sql = "SELECT no, name, email, phone, birth_date FROM customer ORDER BY no";
//        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Customer.class));
//    }
//}