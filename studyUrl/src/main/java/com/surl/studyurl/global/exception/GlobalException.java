package com.surl.studyurl.global.exception;

import com.surl.studyurl.global.httpsdata.RespData;
import lombok.Getter;
import com.surl.studyurl.standard.dto.EmpDto;
@Getter
public class GlobalException extends RuntimeException {
    private final RespData<EmpDto> rsData;

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