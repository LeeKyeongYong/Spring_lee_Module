package com.example.wrpi.global.accounts.config;

import com.example.wrpi.domain.accounts.entity.Account;
import com.example.wrpi.domain.accounts.entity.AccountRole;
import com.example.wrpi.domain.accounts.service.AccountService;
import com.example.wrpi.global.common.TestDescription;
import com.example.wrpi.global.events.common.BaseControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{

        //Given
        String username = "sleekydz86@icloud.com";
        String password = "kylee";
        String clientId= "myApp";
        String clientSecret="pass";

        Account kylee = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN,AccountRole.USER))
                .build();

        this.accountService.saveAccount(kylee);

        this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId,clientSecret))
                .param("username",username)
                .param("password",password)
                .param("grant_type","password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

    }
}
