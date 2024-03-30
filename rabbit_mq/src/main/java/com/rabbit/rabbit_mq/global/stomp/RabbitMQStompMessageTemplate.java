package com.rabbit.rabbit_mq.global.stomp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
impor

@Profile("prod")
@Component
@RequiredArgsConstructor
public class RabbitMQStompMessageTemplate implements StompMessageTemplate {

    private final RabbitTemplate template;

    @Override
    public void convertAndSend(String type, String destination, Object payload) {
        if(type.equals("topic")){
            type="amq."+type;
        }
        template.convertAndSend(type,destination,payload);
    }
}
