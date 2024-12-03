package com.study.nextspring.global.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KwTypeV1 {
    ALL("all"),
    TITLE("title"),
    BODY("body"),
    NAME("name");

    private final String value;
}