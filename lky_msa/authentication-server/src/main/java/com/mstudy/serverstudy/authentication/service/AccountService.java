package com.mstudy.serverstudy.authentication.service;

import com.mstudy.serverstudy.authentication.dto.AccountDTO;
import com.mstudy.serverstudy.authentication.entity.Account;
import com.mstudy.serverstudy.authentication.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    // 계정조회
    public Account selectAccount(AccountDTO accountDTO) {
        Optional<Account> optional = accountRepository.findById(accountDTO.getAccountId());
        if(optional.isPresent()) {
            Account account = optional.get();
            if(account.getPassword().equals(accountDTO.getPassword())) {
                return account;
            }
        }
        return null;
    }

    // 계정정보 저장
    public void saveAccount(AccountDTO accountDTO, String token) {
        accountRepository.save(Account.builder()
                .accountId(accountDTO.getAccountId())
                .password(accountDTO.getPassword())
                .token(token)
                .build());
    }
}