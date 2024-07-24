package com.example.wrpi.domain.accounts.repository;

import com.example.wrpi.domain.accounts.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Integer> {
    Optional<Account> findByEmail(String username);
}
