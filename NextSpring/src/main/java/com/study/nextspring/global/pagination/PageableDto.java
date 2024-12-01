package com.study.nextspring.global.pagination;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
public class PageableDto {
    @NonNull
    private int pageNumber;

    @NonNull
    private int pageSize;

    @NonNull
    private SortDto sort;

    @NonNull
    private long offset;

    @NonNull
    private boolean paged;

    @NonNull
    private boolean unpaged;

    @NonNull
    private boolean last;

    @NonNull
    private int totalPages;

    @NonNull
    private long totalElements;

    @NonNull
    private int size;

    @NonNull
    private int number;

    @NonNull
    private boolean first;

    @NonNull
    private int numberOfElements;

    @NonNull
    private boolean empty;
}
