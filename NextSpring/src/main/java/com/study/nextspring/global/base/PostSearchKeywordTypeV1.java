package com.study.nextspring.global.base;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PostSearchKeywordTypeV1 {
    all("all"),
    title("title"),
    content("content"),
    name("name");

    private final String value;

    public static PostSearchKeywordTypeV1 fromString(String text) {
        for (PostSearchKeywordTypeV1 type : PostSearchKeywordTypeV1.values()) {
            if (type.value.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}