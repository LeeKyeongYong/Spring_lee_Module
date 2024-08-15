package com.mstudy.serverstudy.authentication.controller;

import com.mstudy.serverstudy.authentication.dto.AccountDTO;
import com.mstudy.serverstudy.authentication.dto.ResponseDTO;
import com.mstudy.serverstudy.authentication.entity.Account;
import com.mstudy.serverstudy.authentication.service.AccountService;
import com.mstudy.serverstudy.global.util.JWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="v1/account")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AccountService accountService;

    @RequestMapping(value="/join", method=RequestMethod.POST)
    public ResponseEntity<ResponseDTO> join(@Valid @RequestBody AccountDTO accountDTO) throws Exception{
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
        Account account = accountService.selectAccount(accountDTO);

        if(account != null) {
            responseBuilder.code("100").message("already join user.");
        }else {
            accountService.saveAccount(accountDTO, null);
            responseBuilder.code("200").message("success");
        }

        log.debug("join.account.id = {}", accountDTO.getAccountId());
        return ResponseEntity.ok(responseBuilder.build());
    }

    @RequestMapping(value="/token", method= RequestMethod.POST)
    public ResponseEntity<ResponseDTO> token(@Valid @RequestBody AccountDTO accountDTO) throws Exception{
        ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
        Account account = accountService.selectAccount(accountDTO);

        if(account == null) {
            responseBuilder.code("101").message("Unknown user.");
        }else {
            String token = getToken(accountDTO);
            accountService.saveAccount(accountDTO, token);
            responseBuilder.code("200").message("success");
            responseBuilder.token(token);
        }

        log.debug("token.account.id = {}", accountDTO.getAccountId());
        return ResponseEntity.ok(responseBuilder.build());
    }

    private String getToken(AccountDTO accountDTO) {
        return JWTUtil.generate(accountDTO);
    }
}
