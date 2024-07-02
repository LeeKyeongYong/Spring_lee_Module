package com.ps.idempot.domain.vo;

import java.time.LocalDateTime;

public class PayMoney {

    private final int number;
    private final int expriationYear;
    private final int expirationMonth;
    private  final int password;


    public PayMoney(int number, int expriationYear, int expirationMonth, int password) {
        this.number = number;
        this.expriationYear = expriationYear;
        this.expirationMonth = expirationMonth;
        this.password = password;
    }

    public static PayMoney createWithValidateDate(final int number, final int expirationYear, final int expirationMonth, final int password) {
        validateExpirationDate(expirationYear, expirationMonth);
        return new PayMoney(
                number,
                expirationYear,
                expirationMonth,
                password
        );
    }

    public static void validateExpirationDate(final int expirationYear, final int expirationMonth){

        LocalDateTime serverLocalDate = LocalDateTime.now();
        LocalDateTime userPayExpirationDate = LocalDateTime.of(expirationYear,expirationMonth,1,0,0);

        if(serverLocalDate.isAfter(userPayExpirationDate)){
            throw new IllegalArgumentException("유저 카드가 만료 되었습니다.");
        }

    }
}
