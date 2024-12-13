package org.study.jqboot.domain.board.entity;

import lombok.Getter;

@Getter
public class PageHelper {
    private final int start;
    private final int stop;

    public PageHelper(int linesPerPage, int page) {
        this.start = (page - 1) * linesPerPage + 1;
        this.stop = page * linesPerPage;
    }
}