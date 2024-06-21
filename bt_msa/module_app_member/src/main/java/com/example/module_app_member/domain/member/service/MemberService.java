package com.example.module_app_member.domain.member.service;

import com.example.module_app_member.domain.member.entity.Member;
import com.example.module_app_member.domain.member.repository.MemberRepository;
import com.example.module_common_base.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public RsData<Member> join(String username, String password) {
        return join(username, password, username, "");
    }

    @Transactional
    public RsData<Member> join(String username, String password, String nickname, String profileImgUrl) {
        if (findByUsername(username).isPresent()) {
            return RsData.of("400-2", "이미 존재하는 회원입니다.");
        }

        Member member = Member.builder()
                .username(username)
                .password(password)
                .refreshToken(username)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .build();
        memberRepository.save(member);

        return RsData.of("회원가입이 완료되었습니다.".formatted(member.getUsername()), member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public long count() {
        return memberRepository.count();
    }
}