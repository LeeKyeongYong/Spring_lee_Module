package com.study.nextspring.global.exception;

import com.study.nextspring.global.httpsdata.RespData;

public class ServiceException extends RuntimeException {
    private final String resultCode;
    private final String msg;

    public ServiceException(String resultCode, String msg) {
        super(resultCode + " : " + msg);
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public RespData<Void> getRsData() {
        return RespData.of(resultCode, msg, null);
    }
}