package com.alarmstudy.slacksprig.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MsgServiceTest {

    @Autowired
    private MsgService msgService;

    @Test
    void 메시지_전송_테스트() {
        boolean result = msgService.sendMsg("text", "잘 돌아가는 코드입니다.");
        Assertions.assertEquals(result, true);
    }

}