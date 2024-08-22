package com.mstudy.historyservice.history.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ActiveMQController {

    @JmsListener(destination = "${activemq.broker.topic}", containerFactory = "jmsListenerContainerFactory")
    public void pullHistory(String json) {
        log.info(" - activeMq pull = {}", json);
    }

}