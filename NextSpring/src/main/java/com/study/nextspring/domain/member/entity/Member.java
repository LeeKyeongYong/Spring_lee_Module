package com.study.nextspring.domain.member.entity;

import com.study.nextspring.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseTime {
    @Column(unique = true, length = 30)
    private String username;

    @Column(length = 50)
    private String password;

    @Column(length = 30)
    private String nickname;

    @Column(unique = true, length = 50)
    private String apiKey;

    public String getName() {
        return nickname;
    }

    public boolean isAdmin() {
        return "admin".equals(username);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}