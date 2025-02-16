package com.study.nextspring.domain.member.service;

import com.study.nextspring.domain.auth.service.AuthTokenService;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.repository.MemberRepository;
import com.study.nextspring.global.base.MemberSearchKeywordTypeV1;
import com.study.nextspring.global.base.UtClass;
import com.study.nextspring.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final AuthTokenService authTokenService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public long count() {
        return memberRepository.count();
    }

    public Member join(String username, String password, String nickname) {
        //        memberRepository
        //                .findByUsername(username)
        //                .ifPresent(existingMember -> {
        //
        //                });

        Optional<Member> existingMemberOpt = memberRepository.findByUsername(username);

        if (existingMemberOpt.isPresent()) {
            throw new ServiceException("409-1", "해당 username은 이미 사용중입니다.");
        }

        String encryptedPassword = passwordEncoder.encode(password);

        Member member = Member.builder()
                .username(username)
                .password(encryptedPassword)
                .nickname(nickname)
                .apiKey(UUID.randomUUID().toString())
                .build();

        return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(long authorId) {
        return memberRepository.findById(authorId);
    }

    public Optional<Member> findByApiKey(String apiKey) {
        return memberRepository.findByApiKey(apiKey);
    }

    public String genAccessToken(Member member) {
        return authTokenService.genAccessToken(member);
    }

    public String genAuthToken(Member member) {
        return member.getApiKey() + " " + genAccessToken(member);
    }

    public Member getMemberFromAccessToken(String accessToken) {
        Map<String, Object> payload = authTokenService.payload(accessToken);

        if (payload == null) return null;

        long id = (long) payload.get("id");
        String username = (String) payload.get("username");
        String nickname = (String) payload.get("nickname");

        Member member = new Member(id, username, nickname);

        return member;
    }

    public boolean validatePassword(Member member, String rawPassword) {
        return passwordEncoder.matches(rawPassword, member.getPassword());
    }

    public Page<Member> findByPaged(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));

        return memberRepository.findAll(pageRequest);
    }

    public Page<Member> findByPaged(MemberSearchKeywordTypeV1 searchKeywordType, String searchKeyword, int page, int pageSize) {
        if (UtClass.str.isBlank(searchKeyword)) return findByPaged(page, pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));

        searchKeyword = "%" + searchKeyword + "%";

        return switch (searchKeywordType.toString()) {  // 열거형 상수 이름을 문자열로 변환
            case "username" -> memberRepository.findByUsernameLike(searchKeyword, pageRequest);
            default -> memberRepository.findByNicknameLike(searchKeyword, pageRequest);
        };
    }

}