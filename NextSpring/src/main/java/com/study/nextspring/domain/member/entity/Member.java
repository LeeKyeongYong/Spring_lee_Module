package com.study.nextspring.domain.member.entity;

import com.study.nextspring.domain.member.dto.MemberDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(unique = true)
    private String refreshToken;

    @Column(columnDefinition = "BOOLEAN default false")
    private boolean social;

    @Transient
    private Boolean _isAdmin;

    public MemberDto toDto() {
        return new MemberDto(this);
    }

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }

    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin())
            authorities.add("ROLE_ADMIN");

        return authorities;
    }

    private boolean isAdmin() {
        if(this._isAdmin != null)
            return this._isAdmin;
        this._isAdmin = List.of("system", "admin").contains(getUsername());
        return this._isAdmin;
    }

    public String getName() {
        return nickname; // nickname 필드 반환
    }

    public String getProfileImgUrlOrDefault() {
        return "https://placehold.co/640x640?text=O_O";
    }

    public void setAdmin(boolean admin) {
        this._isAdmin = admin;
    }

    // MemberDto를 Member로 변환하는 메서드
    public void updateFromDto(MemberDto dto) {
        this.nickname = dto.getUsername();
        this.refreshToken = dto.getProfileImgUrl(); // profileImgUrl을 refreshToken에 적용하고 싶다면
        // authorities와 social은 이미 Member에서 관리되므로 적절하게 처리해줍니다.
        this.social = dto.isSocial();
        this.modifyDate = LocalDateTime.now(); // 수정일시 갱신
    }
}
