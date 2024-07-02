package com.ps.idempot.infrastructure;

import com.ps.idempot.domain.vo.PayMoney;
import com.ps.idempot.service.CardValidationRequester;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class VisaCardValidationRequester implements CardValidationRequester {

    private RestTemplate restTemplate;

    @Override
    public void validate(final PayMoney payMoney) {
        // 유효성 검사 기능 추가 추후...
    }

}
