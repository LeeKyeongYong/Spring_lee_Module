package com.study.nextspring.domain.member.entity;

import com.study.nextspring.global.jpa.entity.BaseTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public Member(long id, String username) {
        this.setId(id);
        this.username = username;
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin())
            authorities.add("ROLE_ADMIN");

        return authorities;
    }
}