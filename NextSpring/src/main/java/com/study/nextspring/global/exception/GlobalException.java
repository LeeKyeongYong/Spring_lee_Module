package com.study.nextspring.global.exception;

import com.study.nextspring.global.base.Empty;
import com.study.nextspring.global.httpsdata.RespData;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final RespData<Empty> rsData;

    public GlobalException() {
        this("400-0", "에러");
    }

    public GlobalException(String msg) {
        this("400-0", msg);
    }

    public GlobalException(String resultCode, String msg) {
        super("resultCode=" + resultCode + ",msg=" + msg);
        this.rsData = RespData.of(resultCode, msg);
    }

    public static class E404 extends GlobalException {
        public E404() {
            super("404-0", "데이터를 찾을 수 없습니다.");
        }
    }
}