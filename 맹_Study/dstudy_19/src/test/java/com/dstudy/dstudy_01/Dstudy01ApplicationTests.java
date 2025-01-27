package com.dstudy.dstudy_01;

import org.jooq.DSLContext;
import org.jooq.generated.tables.JLecture;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Dstudy01ApplicationTests {

    @Autowired
    DSLContext dslContext;


    @Test
    void contextLoads() {
        dslContext.select(DSL.count())
                .from(JLecture.LECTURE)
                .limit(10)
                .fetch();
    }
}
