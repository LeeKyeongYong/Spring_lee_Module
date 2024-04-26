package com.fly.clstudy.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class MemberJoinReqBody {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
