package com.study.nextspring.domain.post.dto.res;

public record PostStatisticsResBody(
        long totalPostCount,
        long totalPublishedPostCount,
        long totalListedPostCount
) {
}