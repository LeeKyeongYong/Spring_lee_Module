package com.study.nextspring.global.pagination;


import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
public class PageDto<T> {
    @NonNull
    private PageableDto pageable;

    @NonNull
    private List<T> content;

    public PageDto(Page<T> page) {
        this.pageable = new PageableDto(
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                new SortDto(
                        page.getPageable().getSort().isUnsorted(),
                        page.getPageable().getSort().isSorted(),
                        page.getPageable().getSort().isEmpty()
                ),
                page.getPageable().getOffset(),
                page.getPageable().isPaged(),
                page.getPageable().isUnpaged(),
                page.isLast(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isFirst(),
                page.getNumberOfElements(),
                page.isEmpty()
        );
        this.content = page.getContent();
    }
}