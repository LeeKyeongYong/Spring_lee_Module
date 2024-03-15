package com.surl.studyurl.domain.surl.service;

import com.surl.studyurl.domain.surl.repository.SurlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SurService {

    private final SurlRepository surlRepository;
    public long count() {
        return surlRepository.count();
    }
}
