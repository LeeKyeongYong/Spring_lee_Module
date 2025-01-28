package com.dstudy.dstudy_01.web.response;

import com.dstudy.dstudy_01.subject.SubjectWithStudent;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
public class SubjectWithStudentPagedResponse {
    private final PagedResponse page;
    private final List<SubjectResponse> subjectResponses;

    public SubjectWithStudentPagedResponse(PagedResponse page, List<SubjectWithStudent> filmWithActors) {
        this.page = page;
        this.subjectResponses = filmWithActors.stream()
                .map(SubjectResponse::new)
                .toList();
    }

    @Getter
    public static class SubjectResponse {
        private Integer hours;
        private LocalDate openDate;
        private Long id;
        private String name;

        public SubjectResponse(SubjectWithStudent subjectWithStudent) {
            this.hours = subjectWithStudent.getSubject().getHours();
            this.openDate = subjectWithStudent.getSubject().getOpenDate();
            this.id = subjectWithStudent.getSubject().getId();
            this.name = subjectWithStudent.getSubject().getName();
        }
    }
}