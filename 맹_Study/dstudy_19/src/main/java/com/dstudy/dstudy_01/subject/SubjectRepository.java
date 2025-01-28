package com.dstudy.dstudy_01.subject;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.tables.JStudent;
import org.jooq.generated.tables.JSubject;
import org.jooq.generated.tables.pojos.Subject;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubjectRepository {
    private final DSLContext dslContext;
    public final JSubject SUBJECT = JSubject.SUBJECT;

    public Subject findbyId(long id){
        return dslContext.select(SUBJECT.fields())
                .from(SUBJECT)
                .where(SUBJECT.ID.equal(id))
                .fetchOneInto(Subject.class);
    }

    public SubjectInfo findByIdSubjectInfo(Long id){
        return dslContext.select(SUBJECT.ID,SUBJECT.HOURS,SUBJECT.NAME,SUBJECT.OPEN_DATE)
                .from(SUBJECT)
                .where(SUBJECT.ID.equal(id))
                .fetchOneInto(SubjectInfo.class);
    }

        public List<SubjectWithStudent> findSubjectWithStudentList(Long page,Long pageSize){
            final JStudent STUDENT = JStudent.STUDENT;
            return dslContext.select(
                    DSL.row(SUBJECT.fields()),
                    DSL.row(STUDENT.fields())
            )
                    .from(SUBJECT)
                    .join(STUDENT)
                    .on(SUBJECT.ID.eq(STUDENT.NO))
                    .limit(pageSize)
                    .offset((page-1)*pageSize)
                    .fetchInto(SubjectWithStudent.class);
        }
}
