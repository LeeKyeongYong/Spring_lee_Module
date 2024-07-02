package com.ps.idempot.service;

import com.ps.idempot.domain.vo.PayMoney;

public interface CardValidationRequester {
    void validate(final PayMoney payMoney);
}
