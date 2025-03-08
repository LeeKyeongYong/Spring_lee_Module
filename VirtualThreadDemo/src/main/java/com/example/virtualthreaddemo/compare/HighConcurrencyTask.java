package com.example.virtualthreaddemo.compare;

import java.util.concurrent.Executors;

public class HighConcurrencyTask {

    public static void main(String[] args) {

        int taskCount = 1000000;
        System.out.println("높은 동시성 작업을 위한 일반 스레드 테스트 중....");
        long start = System.currentTimeMillis();
        var  normalExecutor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().factory());
        for(int  i = 0; i < taskCount; i++) {
            normalExecutor.submit(()->simulateTask());
        }
        long end  = System.currentTimeMillis();
        System.out.println("일반 스레드가 소요한 총 실행 시간 " + (end - start) + " ms");

        System.out.println("높은 동시성 작업을 위한 가상 스레드 테스트 중 " );
        long vStart = System.currentTimeMillis();
        var virtualExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());
        for(int  i = 0; i < taskCount; i++) {
            virtualExecutor.submit(()->simulateTask());
        }
        long vEnd  = System.currentTimeMillis();
        System.out.println("가상 스레드가 소요한 총 실행 시간 " + (vEnd - vStart) + " ms");


    }

    private static void simulateTask() {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}