package com.study.nextspring.global.base.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.lang.NonNull;
@Getter
public class PageDto<T> {
    @NonNull
    private int currentPageNumber;
    @NonNull
    private int pageSize;
    @NonNull
    private long totalPages;
    @NonNull
    private long totalItems;
    @NonNull
    private List<T> items;

    public PageDto(Page<T> page) {
        this.currentPageNumber = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.items = page.getContent();
    }
}