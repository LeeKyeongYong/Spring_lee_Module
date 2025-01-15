package org.study.jqboot.global.https;

public class RespData<T> {
    private final String resultCode;
    private final int statusCode;
    private final String msg;
    private final T data;

    public RespData(String resultCode, int statusCode, String msg, T data) {
        this.resultCode = resultCode;
        this.statusCode = statusCode;
        this.msg = msg;
        this.data = data;
    }

    // Static factory methods (equivalent to companion object)
    public static <T> RespData<T> of(String resultCode, String msg, T data) {
        String[] parts = resultCode.split("-", 2);
        int statusCode = 0;
        try {
            statusCode = Integer.parseInt(parts[0]);
        } catch (NumberFormatException ignored) {}

        return new RespData<>(resultCode, statusCode, msg, data);
    }

    public static <T> RespData<T> of(String resultCode, String msg) {
        return of(resultCode, msg, null);
    }

    /*
    public static <T> RespData<T> fromErrorCode(MessageCode errorCode) {
        String[] parts = errorCode.getCode().split("-", 2);
        int statusCode = 0;
        try {
            statusCode = Integer.parseInt(parts[0]);
        } catch (NumberFormatException ignored) {}

        return new RespData<>(
                errorCode.getCode(),
                statusCode,
                errorCode.getMessage(),
                null
        );
    }
    */

    // Method to create new RespData with different data type
    public <U> RespData<U> newDataOf(U newData) {
        return new RespData<>(resultCode, statusCode, msg, newData);
    }

    public boolean isSuccess() {
        return statusCode >= 200 && statusCode < 400;
    }

    public boolean isFail() {
        return !isSuccess();
    }

    // Getters
    public String getResultCode() {
        return resultCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}