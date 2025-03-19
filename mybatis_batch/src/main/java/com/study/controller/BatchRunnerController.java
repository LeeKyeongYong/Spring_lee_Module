package com.study.controller;

import com.study.domain.Dummy;
import com.study.service.DummyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class BatchRunnerController implements CommandLineRunner {

    private static final int COUNT = 2000;

    @Autowired
    private DummyService dummyService;

    @Override
    public void run(String... args) {
        // 데이터 준비
        List<Dummy> dummies = new ArrayList<>();
        for (int i = 0; i < COUNT; i++) {
            dummies.add(new Dummy(null, makeRandomString(), null));
        }

        long start, elapsedTime;

        /*
        // 디폴트 배치 사이즈
        System.out.println("디폴트 배치 사이즈, " + COUNT + "건 입력");
        start = System.currentTimeMillis();
        dummyService.addDummy(dummies);
        elapsedTime = System.currentTimeMillis() - start;
        System.out.println("소요 시간 : " + elapsedTime + "밀리초");
        */

        // 배치 사이즈 별 수행 시간 비교
        int batchSize = 2000;
        System.out.println("배치 사이즈 : " + batchSize + ", " + COUNT + "건 입력");
        start = System.currentTimeMillis();
        dummyService.addDummy(dummies, batchSize);
        elapsedTime = System.currentTimeMillis() - start;
        System.out.println("소요 시간 : " + elapsedTime + "밀리초");
    }

    private String makeRandomString() {
        Random random = new Random();
        int r = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            r = random.nextInt('z' - 'a' + 1);
            builder.append((char) ('a' + r));
        }
        return builder.toString();
    }
}