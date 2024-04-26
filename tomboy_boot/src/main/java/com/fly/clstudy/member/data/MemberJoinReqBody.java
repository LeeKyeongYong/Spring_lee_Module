package com.fly.clstudy.member.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberJoinReqBody {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
}
