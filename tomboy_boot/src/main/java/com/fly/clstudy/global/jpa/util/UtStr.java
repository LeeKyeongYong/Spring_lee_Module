package com.fly.clstudy.global.jpa.util;

import com.fly.clstudy.global.app.AppConfig;
import lombok.SneakyThrows;

public class UtStr {
    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.isBlank();
        }

        public static boolean hasLength(String str) {
            return !isBlank(str);
        }
    }
    public static class json {

        @SneakyThrows
        public static String toString(Object obj) {
            return AppConfig.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
    }
}
