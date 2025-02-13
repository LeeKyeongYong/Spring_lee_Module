package com.study.nextspring.domain.post.dto.res;

import org.springframework.lang.NonNull;
public record PostStatisticsResBody(
        @NonNull
        long totalPostCount,
        @NonNull
        long totalPublishedPostCount,
        @NonNull
        long totalListedPostCount
) {
}