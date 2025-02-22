package com.study.hashing.consistent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

@Slf4j
class ConsistentHashingTests {
    private ConsistentHashingWithVnodes<String> consistentHash;
    private static final int VIRTUAL_NODES = 10;
    private static final int TEST_DATA_SIZE = 1_000_000;

    @BeforeEach
    void setUp() {
        // 테스트 환경 구성
        List<String> initialNodes = Arrays.asList("Node1", "Node2", "Node3");
        consistentHash = new ConsistentHashingWithVnodes<>(VIRTUAL_NODES, initialNodes);
    }

    @Test
    void 데이터_분산_테스트() {
        // 노드별 데이터 분포 확인
        Map<String, Integer> distribution = new HashMap<>();
        Random random = new Random();

        // 대량의 데이터 생성 및 분산 테스트
        for (int i = 0; i < TEST_DATA_SIZE; i++) {
            String key = "data-" + random.nextInt();
            String node = consistentHash.getNodeForKey(key);
            distribution.merge(node, 1, Integer::sum);
        }

        // 분산 결과 검증
        distribution.forEach((node, count) -> {
            double percentage = (count * 100.0) / TEST_DATA_SIZE;
            log.info("노드 {}: {}개 데이터 ({}%)", node, count, String.format("%.2f", percentage));
            assertTrue(percentage > 20 && percentage < 40,
                    "데이터 분산이 균일하지 않음: " + percentage + "%");
        });
    }

    @Test
    void 노드_추가_제거_테스트() {
        // 초기 데이터 위치 기록
        String testKey = "test-data";
        String initialNode = consistentHash.getNodeForKey(testKey);

        // 새 노드 추가
        consistentHash.addNode("Node4");
        String newNode = consistentHash.getNodeForKey(testKey);

        // 기존 노드가 제거되지 않았다면 데이터 위치는 변경되지 않아야 함
        assertEquals(initialNode, newNode, "불필요한 데이터 이동 발생");

        // 노드 제거 후 데이터 재배치 확인
        consistentHash.removeNode(initialNode);
        String relocatedNode = consistentHash.getNodeForKey(testKey);
        assertNotEquals(initialNode, relocatedNode, "노드 제거 후 데이터가 재배치되지 않음");
    }

    @Test
    void 데이터_저장소_통합_테스트() {
        DataManager<String> dataManager = new DataManager<>();
        String testKey = "test-key";
        String testValue = "test-value";

        // 데이터 저장
        String assignedNode = consistentHash.getNodeForKey(testKey);
        dataManager.storeData(testKey, testValue, assignedNode);

        // 데이터 조회 및 검증
        Optional<String> retrieved = dataManager.getData(testKey, assignedNode);
        assertTrue(retrieved.isPresent(), "저장된 데이터를 찾을 수 없음");
        assertEquals(testValue, retrieved.get(), "저장된 데이터가 일치하지 않음");

        // 노드 추가 및 데이터 재배치
        consistentHash.addNode("NewNode");
        dataManager.redistributeData(consistentHash);

        // 재배치 후 데이터 검증
        String newNode = consistentHash.getNodeForKey(testKey);
        Optional<String> afterRedistribution = dataManager.getData(testKey, newNode);
        assertTrue(afterRedistribution.isPresent(), "재배치 후 데이터를 찾을 수 없음");
        assertEquals(testValue, afterRedistribution.get(), "재배치 후 데이터가 일치하지 않음");
    }
}