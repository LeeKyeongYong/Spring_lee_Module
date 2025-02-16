package com.study.nextspring.global.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MemberSearchKeywordTypeV1 {
    username("username"),
    nickname("nickname");

    private final String value;
}