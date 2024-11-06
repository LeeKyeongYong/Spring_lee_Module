package com.study.nextspring.domain.member.member.service;

import com.study.nextspring.domain.member.member.entity.Member;
import com.study.nextspring.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Transactional
    public Member join(String username, String password, String nickname) {
        findByUsername(username).ifPresent(member -> {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        });

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return member;
    }

    public long count() {
        return memberRepository.count();
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}