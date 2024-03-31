package com.example.sb_search.global.standard;

import com.example.sb_search.global.app.AppConfig;
import lombok.SneakyThrows;

public class UtBase {
    public static class json {
        @SneakyThrows
        public static String toString(Object obj) {
            return AppConfig.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
    }
}
