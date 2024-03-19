package com.surl.studyurl.domain.member.entity;

import com.surl.studyurl.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Setter
@Getter
public class Member extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String userid;
    private String password;
    @Column(unique = true)
    private String refreshToken;
    private String username;

    public String getName() {
        return username;
    }

    public List<String> getAuthoritiesAsStringList() {
        if (isAdmin()) List.of("ROLE_ADMIN", "ROLE_MEMBER");

        return List.of("ROLE_MEMBER");
    }

    private boolean isAdmin() {
        return username.equals("admin");
    }
}
