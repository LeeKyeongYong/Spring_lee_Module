package com.study.nextspring.global.httpsdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.nextspring.global.base.Empty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Getter
public class RespData<T> {
        String resultCode;
        int statusCode;
        String msg;
        T data;

    public static RespData<Empty> of(String msg) {
        return of("200-1", msg, new Empty());
    }

    public static <T> RespData<T> of(T data) {
        return of("200-1", "성공", data);
    }

    public static <T> RespData<T> of(String msg, T data) {
        return of("200-1", msg, data);
    }

    public static <T> RespData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, (T) new Empty());
    }

    public static <T> RespData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode.split("-", 2)[0]);

        RespData<T> tRsData = new RespData<>(resultCode, statusCode, msg, data);

        return tRsData;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return getStatusCode() >= 200 && getStatusCode() < 400;
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    public <T> RespData<T> newDataOf(T data) {
        return new RespData<>(resultCode, statusCode, msg, data);
    }
 }