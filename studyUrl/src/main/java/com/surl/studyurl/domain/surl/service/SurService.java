package com.surl.studyurl.domain.surl.service;

import com.surl.studyurl.domain.surl.entity.Surl;
import com.surl.studyurl.domain.surl.repository.SurlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SurService {

    private final SurlRepository surlRepository;
    public long count() {
        return surlRepository.count();
    }

    @Transactional
    public Surl create(String url, String title) {
        Surl surl = Surl.builder()
                .url(url)
                .title(title)
                .build();

        surlRepository.save(surl);

        return surl;
    }
    public Optional<Surl> findById(long id){
        return surlRepository.findById(id);
    }
}
