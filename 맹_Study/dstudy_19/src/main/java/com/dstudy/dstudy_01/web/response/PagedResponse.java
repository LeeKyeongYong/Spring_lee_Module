package com.dstudy.dstudy_01.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse {
    private long page;
    private long pageSize;
}