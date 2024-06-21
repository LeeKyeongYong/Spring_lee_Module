package com.example.module_app_member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberControllerTest {
	@Test
	@DisplayName("index 테스트")
	public void t1() {
		assertThat(10 + 20).isEqualTo(30);
	}
}