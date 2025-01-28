package com.dstudy.dstudy_01;

import com.dstudy.dstudy_01.subject.SubjectInfo;
import com.dstudy.dstudy_01.subject.SubjectRepository;
import com.dstudy.dstudy_01.subject.SubjectService;
import com.dstudy.dstudy_01.web.response.SubjectWithStudentPagedResponse;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JLecture;
import org.jooq.generated.tables.pojos.Subject;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

@SpringBootTest
class Dstudy01ApplicationTests {
    //@Autowired
    //DSLContext dslContext;

//    @Test
//    void contextLoads() {
//        dslContext.select(DSL.count())
//                .from(JLecture.LECTURE)
//                .limit(10)
//                .fetch();
//    }

    @Autowired
    SubjectRepository subjectRepository;

    @Autowired
    SubjectService subjectService;

    @Test
    @DisplayName("1) 점수정보 조회")
    void test(){
        Subject subject = subjectRepository.findbyId(1L);
        Assertions.assertThat(subject).isNotNull();
    }

    @Test
    @DisplayName("2) 점수정보 간략 조회")
    void test2(){
        SubjectInfo subjectInfo = subjectService.getSimpleSubjectInfo(1L);
        Assertions.assertThat(subjectInfo).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("3) 점수와 학생 정보를 페이징하여 조회. - 응답까지")
    void test3() {
        SubjectWithStudentPagedResponse response = subjectService.getSubjectStudentPageResponse(1L, 10L);

        // subjectResponses 리스트가 비어 있지 않은지 검증
        Assertions.assertThat(response.getSubjectResponses())
                .isNotEmpty();

        // 각 SubjectResponse의 특정 필드 값도 검증 가능
        Assertions.assertThat(response.getSubjectResponses())
                .extracting("name")
                .isNotEmpty();
    }

}
