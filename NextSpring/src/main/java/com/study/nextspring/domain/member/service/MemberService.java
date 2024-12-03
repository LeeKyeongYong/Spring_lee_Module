package com.study.nextspring.domain.member.service;

import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.repository.MemberRepository;
import com.study.nextspring.global.exception.GlobalException;
import com.study.nextspring.global.httpsdata.RespData;
import com.study.nextspring.global.security.SecurityUser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthTokenService authTokenService;
    private final PasswordEncoder passwordEncoder;


    private boolean passwordMatches(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }


    // 조회
    public long count() {
        return memberRepository.count();
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    // 처리
    @Transactional
    public Member join(String username, String password, String nickname) {
        findByUsername(username).ifPresent(member -> {
            throw new GlobalException("409-1", "이미 존재하는 아이디입니다.");
        });

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .refreshToken(authTokenService.genRefreshToken())
                .build();

        memberRepository.save(member);

        return member;
    }


    // 인증
    public record MemberAuthAndMakeTokensResBody(
            @NonNull Member member,
            @NonNull String accessToken,
            @NonNull String refreshToken
    ) {
    }

    public RespData<MemberAuthAndMakeTokensResBody> authAndMakeTokens(String username, String password) {
        Member member = findByUsername(username).orElseThrow(
                () -> new GlobalException("401-1", "아이디가 존재하지 않습니다.")
        );

        if (!passwordMatches(member, password))
            throw new GlobalException("401-1", "비밀번호가 일치하지 않습니다.");

        String refreshToken = member.getRefreshToken();
        String accessToken = authTokenService.genAccessToken(member);

        return RespData.of(
                "%s님 안녕하세요.".formatted(member.getUsername()),
                new MemberAuthAndMakeTokensResBody(member, accessToken, refreshToken)
        );
    }

    @Transactional(propagation = NOT_SUPPORTED)
    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = authTokenService.getDataFrom(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<String> authorities = (List<String>) payloadBody.get("authorities");

        return new SecurityUser(
                id,
                username,
                "",
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
    }

    @Transactional(propagation = NOT_SUPPORTED)
    public boolean validateToken(String token) {
        return authTokenService.validateToken(token);
    }

    @Transactional(propagation = NOT_SUPPORTED)
    public RespData<String> refreshAccessToken(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken).get();

        String accessToken = authTokenService.genAccessToken(member);

        return RespData.of("201-1", "토큰 갱신 성공", accessToken);
    }
}