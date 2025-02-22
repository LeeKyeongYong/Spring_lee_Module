package com.study.hashing;

import com.study.hashing.consistent.ConsistentHashingWithVnodes;
import com.study.hashing.consistent.DataManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SArcApplication {

	public static void main(String[] args) {
		// 초기 노드 리스트
		List<String> initialNodes = Arrays.asList("Node1", "Node2", "Node3");

		// 일관된 해싱 객체 생성 (각 노드당 3개의 가상 노드 할당)
		ConsistentHashingWithVnodes<String> consistentHash = new ConsistentHashingWithVnodes<>(3, initialNodes);

		// 데이터 관리자 생성
		DataManager<String> dataManager = new DataManager<>();

		// 데이터 저장
		String key1 = "User123";
		String value1 = "UserData123";
		String assignedNode1 = consistentHash.getNodeForKey(key1);
		dataManager.storeData(key1, value1, assignedNode1);

		String key2 = "User456";
		String value2 = "UserData456";
		String assignedNode2 = consistentHash.getNodeForKey(key2);
		dataManager.storeData(key2, value2, assignedNode2);

		// 데이터 조회
		Optional<String> retrievedData1 = dataManager.getData(key1, assignedNode1);
		Optional<String> retrievedData2 = dataManager.getData(key2, assignedNode2);

		System.out.println("조회된 데이터 1: " + retrievedData1.orElse("데이터 없음"));
		System.out.println("조회된 데이터 2: " + retrievedData2.orElse("데이터 없음"));

		// 새로운 노드 추가
		System.out.println("노드 'Node4' 추가 후 데이터 재배치 실행...");
		consistentHash.addNode("Node4");
		dataManager.redistributeData(consistentHash);

		// 데이터 재조회 (노드 변경 후)
		String newAssignedNode1 = consistentHash.getNodeForKey(key1);
		String newAssignedNode2 = consistentHash.getNodeForKey(key2);

		Optional<String> newRetrievedData1 = dataManager.getData(key1, newAssignedNode1);
		Optional<String> newRetrievedData2 = dataManager.getData(key2, newAssignedNode2);

		System.out.println("재배치 후 조회된 데이터 1: " + newRetrievedData1.orElse("데이터 없음"));
		System.out.println("재배치 후 조회된 데이터 2: " + newRetrievedData2.orElse("데이터 없음"));

	}
}
