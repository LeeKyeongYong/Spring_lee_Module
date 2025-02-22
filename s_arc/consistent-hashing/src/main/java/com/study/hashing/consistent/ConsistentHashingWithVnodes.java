package com.study.hashing.consistent;

import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ConsistentHashingWithVnodes<T> {
    // 가상 노드의 수를 지정하는 상수
    private final int numberOfReplicas;

    // 해시 링을 구현하는 정렬된 맵
    @Getter
    private final SortedMap<BigInteger, T> circle = new TreeMap<>();

    // 해시 알고리즘 상수
    private static final String HASH_ALGORITHM = "SHA-1";

    /**
     * 생성자: 초기 노드들과 가상 노드 수를 설정
     * @param numberOfReplicas 각 실제 노드당 생성할 가상 노드의 수
     * @param nodes 초기 노드 컬렉션
     */
    public ConsistentHashingWithVnodes(int numberOfReplicas, Collection<T> nodes) {
        this.numberOfReplicas = numberOfReplicas;
        nodes.forEach(this::addNode);
    }

    /**
     * 노드를 해시 링에 추가
     * @param node 추가할 노드
     */
    public void addNode(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            BigInteger hash = hashFor(node.toString() + i);
            circle.put(hash, node);
            log.info("가상 노드 추가: {} (해시: {})", node, hash);
        }
    }

    /**
     * 노드를 해시 링에서 제거
     * @param node 제거할 노드
     */
    public void removeNode(T node) {
        for (int i = 0; i < numberOfReplicas; i++) {
            BigInteger hash = hashFor(node.toString() + i);
            circle.remove(hash);
            log.info("가상 노드 제거: {} (해시: {})", node, hash);
        }
    }

    /**
     * 주어진 키에 해당하는 노드 찾기
     * @param key 찾을 키
     * @return 키가 할당될 노드
     */
    public T getNodeForKey(String key) {
        if (circle.isEmpty()) {
            throw new IllegalStateException("해시 링에 노드가 없습니다.");
        }

        BigInteger hash = hashFor(key);
        SortedMap<BigInteger, T> tailMap = circle.tailMap(hash);

        BigInteger nodeHash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        T node = circle.get(nodeHash);

        log.debug("키 '{}' (해시: {})가 노드 '{}'에 할당됨", key, hash, node);
        return node;
    }

    /**
     * SHA-1 해시 생성
     * @param key 해시할 키
     * @return 해시 값
     */
    private BigInteger hashFor(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] digest = md.digest(key.getBytes());
            return new BigInteger(1, digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해시 알고리즘 오류: " + e.getMessage(), e);
        }
    }


}