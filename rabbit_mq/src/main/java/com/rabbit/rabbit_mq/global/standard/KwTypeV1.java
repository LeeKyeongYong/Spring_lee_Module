package com.rabbit.rabbit_mq.global.standard;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KwTypeV1 {
    ALL("all"),
    TITLE("title"),
    BODY("body"),
    NAME("name");

    private final String value;
}