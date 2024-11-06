package com.study.nextspring.domain.member.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Setter(value = AccessLevel.PRIVATE)
    private LocalDateTime modifyDate;

    @Column(unique = true)
    private String username;

    private String password;

    private String nickname;

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }
}