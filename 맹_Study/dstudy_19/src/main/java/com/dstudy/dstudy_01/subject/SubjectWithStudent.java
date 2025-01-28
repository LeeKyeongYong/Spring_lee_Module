package com.dstudy.dstudy_01.subject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jooq.generated.tables.pojos.*;

@Getter
@RequiredArgsConstructor
public class SubjectWithStudent {

    private final Subject subject;
    private final Student student;

    private Long  getSubjectId(){
        return subject.getId();
    }

    public String getFullStudentName(){
        return student.getName()+" "+student.getBirthDate();
    }

}
