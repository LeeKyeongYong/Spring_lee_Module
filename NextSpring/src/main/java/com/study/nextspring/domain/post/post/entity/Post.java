package com.study.nextspring.domain.post.post.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Post {
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
    private String title;
    private String body;

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }
}
