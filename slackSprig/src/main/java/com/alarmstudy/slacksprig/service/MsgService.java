package com.alarmstudy.slacksprig.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import com.slack.api.webhook.WebhookResponse;

@Service
@Slf4j
public class MsgService {

    @Value("${slack.webhook.alertbot.url}")
    String webhookUrl;

    public boolean sendMsg(String key, String message){
        Slack slack = Slack.getInstance();
        String payload = makePayLoad(key, message);
        try {
            WebhookResponse response = slack.send(webhookUrl, payload);
            System.out.println(response);
        } catch (IOException e) {
            log.error("slack 메시지 발송 중 문제가 발생했습니다.", e.toString());
            throw new RuntimeException(e);
        }

        return true;
    }

    private String makePayLoad(String key, String message){
        return "{\"" + key + "\":\"" + message + "\"}";
    }

}
