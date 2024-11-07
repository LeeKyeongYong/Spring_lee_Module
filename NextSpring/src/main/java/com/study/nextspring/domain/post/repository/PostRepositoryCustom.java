package com.study.nextspring.domain.post.repository;

import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.post.entity.Post;
import com.study.nextspring.global.base.KwTypeV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findByKw(KwTypeV1 kwType, String kw, Member author, Boolean published, Boolean listed, Pageable pageable);
}