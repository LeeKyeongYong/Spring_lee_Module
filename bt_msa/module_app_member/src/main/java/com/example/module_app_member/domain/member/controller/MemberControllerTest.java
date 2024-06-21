package com.example.module_app_member.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MemberControllerTest {
    @Test
    @DisplayName("index 테스트")
    public void t1() {
        assertThat(10 + 20).isEqualTo(30);
    }
}