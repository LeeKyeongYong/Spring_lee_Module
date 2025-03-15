package com.study.aop009.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;

import com.study.aop009.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDaoImpl implements CustomerDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addCustomer(final Customer customer) {
        final String sql = "insert into customer(name, email, phone, birth_date) values(?,?,?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, customer.getName());
                pstmt.setString(2, customer.getEmail());
                pstmt.setString(3, customer.getPhone());
                pstmt.setDate(4, new java.sql.Date(customer.getBirthDate().getTime()));
                return pstmt;
            }
        }, holder);
        customer.setNo(holder.getKey().intValue());
    }

    @Override
    public List<Customer> getAllCustomers() {
        final String sql = "select no, name, email, phone, birth_date from customer order by no";
        List<Customer> customerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Customer>(Customer.class));
        return customerList;
    }
}