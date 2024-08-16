package com.study.mstudy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.mstudy.item.controller.ItemController;
import com.study.mstudy.item.dto.ItemDTO;
import com.study.mstudy.item.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Slf4j
@SpringBootTest(webEnvironment=WebEnvironment.MOCK)
class MstudyApplicationTests {
    @Autowired
    private ItemService itemService;

    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void setup(){
        mvc = MockMvcBuilders.standaloneSetup(new ItemController(itemService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("물품등록 테스트")
    void test() throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            log.info(objectMapper.writeValueAsString(getTestItem()) + "");

            mvc.perform(post("/v1/item/add/C")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("accountId", "admin")
                            .accept(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(getTestItem())))
                    .andDo(print())                        // 결과를 print. MockMvcBuilders의 alwaysDo(print())로 대체 가능
                    .andExpect(status().isOk());           // 호출 결과값이 OK가 나오면 정상처리
        } catch (Exception e) {
            fail(ExceptionUtils.getStackTrace(e));
        }
    }

    private ItemDTO getTestItem() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId("test1");
        itemDTO.setName("상품명테스트");
        itemDTO.setDescription("상품 설명테스트");
        itemDTO.setCount(50);
        return itemDTO;
    }

}
