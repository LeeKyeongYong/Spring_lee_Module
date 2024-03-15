package com.surl.studyurl.domain.member.service;

import com.surl.studyurl.domain.member.entity.Member;
import com.surl.studyurl.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member create(String userid,String password,String username){
        Member member = Member.builder()
                .userid(userid)
                .password(passwordEncoder.encode(password))
                .username(username)
                .build();

        return memberRepository.save(member);
    }

    public Optional<Member> findByUserNo(long id){
        return memberRepository.findById(id);
    }
}
