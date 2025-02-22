package com.study.hashing.consistent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataStorage {
    // 실제 데이터 저장소
    private final Map<String, String> storage = new ConcurrentHashMap<>();

    /**
     * 데이터 저장
     * @param key 데이터 키
     * @param value 데이터 값
     */
    public void store(String key, String value) {
        storage.put(key, value);
        log.debug("데이터 저장됨: key='{}'", key);
    }

    /**
     * 데이터 조회
     * @param key 데이터 키
     * @return 저장된 값
     */
    public String get(String key) {
        return storage.get(key);
    }

    /**
     * 모든 데이터 조회
     * @return 저장된 모든 데이터의 복사본
     */
    public Map<String, String> getAllData() {
        return new HashMap<>(storage);
    }
}