package com.dstudy.dstudy_01;

import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dstudy01ApplicationTests {

    @Autowired
    private DSLContext dslContext;


    @Test
    void contextLoads() {
    }

}
