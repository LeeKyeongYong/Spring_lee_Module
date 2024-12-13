package org.study.jqboot.domain.board.entity;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Board {
    private Integer no;
    private String writer;
    private String password;
    private String title;
    private String content;
    private LocalDateTime wdate;
    private Integer readCnt;
    private Integer replyCnt;
    private Integer groupNo;
    private Integer sequenceInGroup;
    private Integer indentInGroup;
    private Integer refNo;
}