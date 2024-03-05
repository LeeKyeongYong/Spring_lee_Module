package com.sbstudy.basic_lock2.domain.post.entity;

import com.sbstudy.basic_lock2.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
@Setter
public class Post extends BaseTime {
    private String title;
    @Version
    private Long version;
}