package com.fly.clstudy.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberJoinReqBody {
    private String username;
    private String password;
    private String nickname;
}
