package com.rabbit.rabbit_mq.global.standard;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KwTypeV2 {
    ALL("all"),
    NAME("name"),
    OWNER("OWNER");

    private final String value;
}