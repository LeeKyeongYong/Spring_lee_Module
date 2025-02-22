package com.study.lock.distributedlock.mysql;

public class ExceptionUtil {
    public static String getErrorMessage(Throwable e) {
        if (e == null) return null;

        String message = e.getMessage();
        if (message != null && !message.isEmpty()) {
            return message;
        }

        return e.getClass().getName();
    }
}