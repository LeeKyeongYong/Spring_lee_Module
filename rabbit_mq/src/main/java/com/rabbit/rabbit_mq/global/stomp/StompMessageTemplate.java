package com.rabbit.rabbit_mq.global.stomp;

public interface StompMessageTemplate {
    void convertAndSend(String type,String destination,Object payload);
}
