package com.study.nextspring.global.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum KwTypeV1 {
    ALL("all"),
    TITLE("title"),
    CONTENT("content"),
    NAME("name");

    private final String value;

    public static KwTypeV1 fromString(String text) {
        for (KwTypeV1 type : KwTypeV1.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}