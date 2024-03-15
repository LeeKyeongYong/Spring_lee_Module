package com.surl.studyurl.domain.member.repository;

import com.surl.studyurl.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByUserid(String userid);
}
