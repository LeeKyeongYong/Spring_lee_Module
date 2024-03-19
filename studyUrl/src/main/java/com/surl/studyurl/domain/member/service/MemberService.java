package com.surl.studyurl.domain.member.service;

import com.surl.studyurl.domain.member.entity.Member;
import com.surl.studyurl.domain.member.repository.MemberRepository;
import com.surl.studyurl.global.exception.GlobalException;
import com.surl.studyurl.global.httpsdata.RespData;
import lombok.NonNull;
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
    private final AuthTokenService authTokenService;

    @Transactional
    public Member create(String userid,String password,String username){
        Member member = Member.builder()
                .userid(userid)
                .password(passwordEncoder.encode(password))
                .refreshToken(authTokenService.genRefreshToken())
                .username(username)
                .build();

        return memberRepository.save(member);
    }

    public Optional<Member> findByUserNo(long id){
        return memberRepository.findById(id);
    }

    public Member getRefenceByNo(long id){
        return memberRepository.getReferenceById(id);
    }

    public record AuthAndMakeTokensResponseBody(
            @NonNull Member member,
            @NonNull String accessToken,
            @NonNull String refreshToken
    ) {
    }

    @Transactional
    public RespData <AuthAndMakeTokensResponseBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username)
                .orElseThrow(() -> new GlobalException("400-1", "해당 유저가 존재하지 않습니다."));

        if (!passwordMatches(member, password))
            throw new GlobalException("400-2", "비밀번호가 일치하지 않습니다.");

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        return RespData.of(
                "%s님 안녕하세요.".formatted(member.getUsername()),
                new AuthAndMakeTokensResponseBody(member, accessToken, refreshToken)
        );
    }

    private boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    private Optional<Member> findByUsername(String username) {
        return memberRepository.findByUserid(username);
    }

}
