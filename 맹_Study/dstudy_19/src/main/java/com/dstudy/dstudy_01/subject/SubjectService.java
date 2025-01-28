package com.dstudy.dstudy_01.subject;


import com.dstudy.dstudy_01.web.response.PagedResponse;
import com.dstudy.dstudy_01.web.response.SubjectWithStudentPagedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectWithStudentPagedResponse getFilmActorPageResponse (Long page, Long pageSize) {
        List<SubjectWithStudent> filmWithActorsList = subjectRepository.findSubjectWithStudentList(page, pageSize);
        return new SubjectWithStudentPagedResponse(
                new PagedResponse(page, pageSize),
                filmWithActorsList
        );
    }

    public SubjectInfo getSimpleFilmInfo (Long subjectId) {
        return subjectRepository.findByIdSubjectInfo(subjectId);
    }
}
