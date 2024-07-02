package com.ps.idempot.infrastructure;

import com.ps.idempot.domain.vo.PayMoney;
import com.ps.idempot.service.CardValidationRequester;
import org.springframework.web.client.RestTemplate;

public class VisaCardValidationRequester implements CardValidationRequester {

    private RestTemplate restTemplate;

    @Override
    public void validate(final PayMoney payMoney) {
        // 유효성 검사 기능 추가 추후...
    }

}
