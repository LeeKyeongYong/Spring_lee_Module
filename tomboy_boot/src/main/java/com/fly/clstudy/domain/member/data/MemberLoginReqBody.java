package com.fly.clstudy.domain.member.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberLoginReqBody {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
