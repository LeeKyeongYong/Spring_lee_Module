package com.mserver.gatewayserver.account.service;

import com.mserver.gatewayserver.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    // 토큰과 사용자ID를 통해 row여부조회
    public boolean existsByAccountIdAndToken(String accountId, String token) {
        return accountRepository.existsByAccountIdAndToken(accountId, token);
    }
}