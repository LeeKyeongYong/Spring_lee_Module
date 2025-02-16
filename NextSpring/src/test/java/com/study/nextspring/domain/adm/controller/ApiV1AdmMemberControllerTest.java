package com.study.nextspring.domain.adm.controller;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.base.MemberSearchKeywordTypeV1;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
@Transactional
public class ApiV1AdmMemberControllerTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("다건 조회")
    @WithUserDetails("admin")
    void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/adm/members?page=1&pageSize=3")
                )
                .andDo(print());

        Page<Member> memberPage = memberService
                .findByPaged(1, 3);

        resultActions
                .andExpect(handler().handlerType(ApiV1AdmMemberController.class))
                .andExpect(handler().methodName("items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(memberPage.getTotalElements()))
                .andExpect(jsonPath("$.totalPages").value(memberPage.getTotalPages()))
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(3));

        List<Member> members = memberPage.getContent();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            resultActions
                    .andExpect(jsonPath("$.items[%d].id".formatted(i)).value(member.getId()))
                    .andExpect(jsonPath("$.items[%d].createDate".formatted(i)).value(Matchers.startsWith(member.getCreateDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.items[%d].modifyDate".formatted(i)).value(Matchers.startsWith(member.getModifyDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.items[%d].username".formatted(i)).value(member.getUsername()))
                    .andExpect(jsonPath("$.items[%d].nickname".formatted(i)).value(member.getName()));
        }
    }

    @Test
    @DisplayName("다건 조회 with user1, 403")
    @WithUserDetails("user1")
    void t2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/adm/members?page=1&pageSize=3")
                )
                .andDo(print());

        resultActions
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.resultCode").value("403-1"))
                .andExpect(jsonPath("$.msg").value("권한이 없습니다."));
    }

    @Test
    @DisplayName("다건 조회 with searchKeyword=user")
    @WithUserDetails("admin")
    void t19() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/adm/members?page=1&pageSize=3&searchKeyword=user")
                )
                .andDo(print());

        Page<Member> memberPage = memberService
                .findByPaged(MemberSearchKeywordTypeV1.username, "user", 1, 3);

        resultActions
                .andExpect(handler().handlerType(ApiV1AdmMemberController.class))
                .andExpect(handler().methodName("items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(memberPage.getTotalElements()))
                .andExpect(jsonPath("$.totalPages").value(memberPage.getTotalPages()))
                .andExpect(jsonPath("$.currentPageNumber").value(1))
                .andExpect(jsonPath("$.pageSize").value(3));

        List<Member> members = memberPage.getContent();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            resultActions
                    .andExpect(jsonPath("$.items[%d].id".formatted(i)).value(member.getId()))
                    .andExpect(jsonPath("$.items[%d].createDate".formatted(i)).value(Matchers.startsWith(member.getCreateDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.items[%d].modifyDate".formatted(i)).value(Matchers.startsWith(member.getModifyDate().toString().substring(0, 20))))
                    .andExpect(jsonPath("$.items[%d].username".formatted(i)).value(member.getUsername()))
                    .andExpect(jsonPath("$.items[%d].nickname".formatted(i)).value(member.getName()));
        }
    }

    @Test
    @DisplayName("단건조회 3")
    @WithUserDetails("admin")
    void t3() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/adm/members/3")
                )
                .andDo(print());

        Member member = memberService.findById(3).get();

        resultActions
                .andExpect(handler().handlerType(ApiV1AdmMemberController.class))
                .andExpect(handler().methodName("item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.createDate").value(Matchers.startsWith(member.getCreateDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.modifyDate").value(Matchers.startsWith(member.getModifyDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.username").value(member.getUsername()))
                .andExpect(jsonPath("$.nickname").value(member.getName()));
    }
}