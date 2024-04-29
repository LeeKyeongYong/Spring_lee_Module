package com.fly.clstudy.domain.article.entity;

import com.fly.clstudy.global.jpa.entity.BaseTime;
import com.fly.clstudy.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Builder
@Getter
@Entity
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Article extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    private String title;
    private String body;
    @ManyToOne
    private Member author;
}