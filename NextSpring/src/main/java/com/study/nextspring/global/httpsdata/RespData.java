package com.study.nextspring.global.httpsdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.study.nextspring.global.base.Empty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Getter
public class RespData<T> {
    @NonNull private String resultCode;
    @NonNull private String msg;
    @NonNull private T data;
    @NonNull private int statusCode;

    // 기본 생성자
    public RespData(String resultCode, int statusCode, String msg, T data) {
        this.resultCode = resultCode;
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }

    // 메시지만 받는 생성자
    public static RespData<Empty> of(String msg) {
        return of("200-1", msg, new Empty());
    }

    // 데이터만 받는 생성자
    public static <T> RespData<T> of(T data) {
        return of("200-1", "성공", data);
    }

    // 메시지와 데이터를 받는 생성자
    public static <T> RespData<T> of(String msg, T data) {
        return of("200-1", msg, data);
    }

    // 결과 코드와 메시지를 받는 생성자
    public static RespData<Empty> of(String resultCode, String msg) {
        return of(resultCode, msg, new Empty());
    }

    // 모든 필드를 받는 생성자
    public static <T> RespData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode.split("-", 2)[0]);
        return new RespData<>(resultCode, statusCode, msg, data);
    }

    // 상태 확인 메서드
    @JsonIgnore
    public boolean isSuccess() {
        return getStatusCode() >= 200 && getStatusCode() < 400;
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    // 새로운 데이터로 복사하는 메서드
    public <R> RespData<R> newDataOf(R data) {
        return new RespData<>(resultCode, statusCode, msg, data);
    }
}