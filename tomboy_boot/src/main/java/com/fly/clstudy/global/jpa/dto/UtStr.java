package com.fly.clstudy.global.jpa.dto;

public class UtStr {
    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.isBlank();
        }

        public static boolean hasLength(String str) {
            return !isBlank(str);
        }
    }
}
