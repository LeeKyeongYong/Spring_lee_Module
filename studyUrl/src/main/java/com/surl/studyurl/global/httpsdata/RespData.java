package com.surl.studyurl.global.httpsdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.surl.studyurl.standard.dto.EmpDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Getter
public class RespData<T> {
    public static final RespData<EmpDto> OK = of("200-1", "성공", new EmpDto());
    @NonNull
    String resultCode;
    @NonNull
    int statusCode;
    @NonNull
    String msg;
    @NonNull
    T data;

    public static RespData<EmpDto> of(String msg) {
        return of("200-1", msg, new EmpDto());
    }

    public static <T> RespData<T> of(T data) {
        return of("200-1", "성공", data);
    }

    public static <T> RespData<T> of(String msg, T data) {
        return of("200-1", msg, data);
    }

    public static <T> RespData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, (T) new EmpDto());
    }

    public static <T> RespData<T> of(String resultCode, String msg, T data) {
        int statusCode = Integer.parseInt(resultCode.split("-", 2)[0]);

        RespData<T> tRsData = new RespData<>(resultCode, statusCode, msg, data);

        return tRsData;
    }

    @NonNull
    @JsonIgnore
    public boolean isSuccess() {
        return getStatusCode() >= 200 && getStatusCode() < 400;
    }

    @NonNull
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }

    public <T> RespData<T> newDataOf(T data) {
        return new RespData<>(resultCode, statusCode, msg, data);
    }
}
