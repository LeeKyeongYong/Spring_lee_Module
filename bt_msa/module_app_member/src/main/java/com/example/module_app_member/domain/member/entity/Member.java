package com.example.module_app_member.domain.member.entity;

import com.example.module_app_member.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.List;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Builder
@Getter
@Setter
public class Member extends BaseTime {
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String refreshToken;
    private String nickname;
    private String profileImgUrl;

    public String getName() {
        return nickname;
    }

    public boolean isAdmin() {
        return List.of("system", "admin").contains(getUsername());
    }
}