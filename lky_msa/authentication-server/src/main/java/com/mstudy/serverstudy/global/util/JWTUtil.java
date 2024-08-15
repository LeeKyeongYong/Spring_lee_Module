package com.mstudy.serverstudy.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mstudy.serverstudy.authentication.dto.AccountDTO;
import org.apache.commons.lang.time.DateUtils;

import java.util.Date;

public class JWTUtil {
    public static String generate(AccountDTO accountDTO) {
        Date now = new Date();
        return JWT.create()
                .withSubject(accountDTO.getAccountId())
                .withExpiresAt(DateUtils.addSeconds(now, 10))
                .withIssuedAt(now)
                .sign(Algorithm.HMAC512("secretKey"));
    }
}