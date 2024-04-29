package com.fly.clstudy.sur.repository;

import com.fly.clstudy.member.entity.Member;
import com.fly.clstudy.sur.entity.Surl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurlRepository extends JpaRepository<Surl, Long> {
    List<Surl> findByAuthorOrderByIdDesc(Member author);
}
