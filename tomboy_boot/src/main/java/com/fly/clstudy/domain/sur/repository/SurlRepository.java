package com.fly.clstudy.domain.sur.repository;

import com.fly.clstudy.domain.member.entity.Member;
import com.fly.clstudy.domain.sur.entity.Surl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurlRepository extends JpaRepository<Surl, Long> {
    List<Surl> findByAuthorOrderByIdDesc(Member author);
}
