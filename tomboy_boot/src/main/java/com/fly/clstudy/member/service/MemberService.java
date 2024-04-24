package com.fly.clstudy.member.service;

import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public RespData<Member> join(String username, String password, String nickname) {
        boolean present = findByUsername(username).isPresent();

        if (present) {
            return RespData.of("400-1", "이미 존재하는 아이디입니다.", Member.builder().build());
        }

        Member member = Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();

        memberRepository.save(member);

        return RespData.of("회원가입이 완료되었습니다.", member);
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
