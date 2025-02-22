package com.study.hashing.consistent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DataManager<T> {
    // 노드별 데이터 저장소 관리
    private final Map<T, DataStorage> nodeDataMap = new ConcurrentHashMap<>();

    /**
     * 데이터를 특정 노드에 저장
     * @param dataKey 데이터 키
     * @param dataValue 데이터 값
     * @param node 저장할 노드
     */
    public void storeData(String dataKey, String dataValue, T node) {
        nodeDataMap.computeIfAbsent(node, k -> new DataStorage())
                .store(dataKey, dataValue);
        log.info("데이터 저장: 키='{}', 노드='{}'", dataKey, node);
    }

    /**
     * 특정 노드에서 데이터 조회
     * @param dataKey 데이터 키
     * @param node 조회할 노드
     * @return 저장된 데이터 값
     */
    public Optional<String> getData(String dataKey, T node) {
        return Optional.ofNullable(nodeDataMap.get(node))
                .map(storage -> storage.get(dataKey));
    }

    /**
     * 노드 변경 시 데이터 재배치
     * @param consistentHash 일관된 해시 인스턴스
     */
    public void redistributeData(ConsistentHashingWithVnodes<T> consistentHash) {
        Map<String, String> allData = new HashMap<>();
        nodeDataMap.values().forEach(storage ->
                allData.putAll(storage.getAllData())
        );

        nodeDataMap.clear();
        allData.forEach((key, value) -> {
            T newNode = consistentHash.getNodeForKey(key);
            storeData(key, value, newNode);
            log.info("데이터 재배치: 키='{}', 새 노드='{}'", key, newNode);
        });
    }
}