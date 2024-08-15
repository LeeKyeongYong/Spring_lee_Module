package com.mserver.gatewayserver.account.repository;

import com.mserver.gatewayserver.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    boolean existsByAccountIdAndToken(String accountId, String token);
}