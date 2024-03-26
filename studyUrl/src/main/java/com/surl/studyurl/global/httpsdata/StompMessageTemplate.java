package com.surl.studyurl.global.httpsdata;

public interface StompMessageTemplate {
    void convertAndSend(String type, String destination, Object payload);
}