package com.fly.clstudy.sur.service;

import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.sur.entity.Surl;
import com.fly.clstudy.sur.repository.SurlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SurlService {
    private final SurlRepository surlRepository;

    public List<Surl> findAll() {
        return surlRepository.findAll();
    }

    @Transactional
    public RespData<Surl> add(Member author, String body, String url) {
        Surl surl = Surl.builder()
                .body(body)
                .url(url)
                .author(author)
                .build();

        surlRepository.save(surl);

        return RespData.of("%d번 URL이 생성되었습니다.".formatted(surl.getId()), surl);
    }

    public Optional<Surl> findById(long id) {
        return surlRepository.findById(id);
    }

    @Transactional
    public void increaseCount(Surl surl) {
        surl.increaseCount();
    }

    @Transactional
    public void delete(Surl surl) {
        surlRepository.delete(surl);
    }

    public List<Surl> findByAuthorOrderByIdDesc(Member author){
        return surlRepository.findByAuthorOrderByIdDesc(author);
    }
}
