package com.study.mbatch2.controller;

import com.study.mbatch2.domain.Dummy;
import com.study.mbatch2.service.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class MainBatchController implements CommandLineRunner {
    private static final int COUNT = 2000;

    @Autowired
    private DummyService service;

    @Override
    public void run(String... args) throws Exception {
        // 데이터 준비
        List<Dummy> dummies = new ArrayList<>();
        for(int i=0; i < COUNT; i++) {
            dummies.add(new Dummy(null, makeRandomString(), null));
        }

        // 배치 처리하지 않고 입력하기
        System.out.println("테스트 1. 개별 트랜잭션 - " + COUNT + "건 입력");
        long start = System.currentTimeMillis();
        service.addDummyWithoutBatch(dummies);
        long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("소요 시간 : " + elapsedTime + "밀리초");

        // 배치 처리하여 입력하기
        System.out.println("테스트 2. 단일 트랜잭션 - " + COUNT + "건 입력");
        start = System.currentTimeMillis();
        service.addDummyWithBatch(dummies);
        elapsedTime = System.currentTimeMillis() - start;
        System.out.println("소요 시간 : " + elapsedTime + "밀리초");
    }

    private static String makeRandomString() {
        Random random = new Random();
        int r = 0;
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<500; i++) {
            r = random.nextInt('z' - 'a' + 1);
            builder.append((char)('a' + r));
        }

        return builder.toString();
    }
}
