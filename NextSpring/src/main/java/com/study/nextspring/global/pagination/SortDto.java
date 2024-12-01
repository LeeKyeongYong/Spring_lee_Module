package com.study.nextspring.global.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.NonNull;

@AllArgsConstructor
@Getter
public class SortDto {
    @NonNull
    boolean empty;
    @NonNull
    boolean sorted;
    @NonNull
    boolean unsorted;
}
