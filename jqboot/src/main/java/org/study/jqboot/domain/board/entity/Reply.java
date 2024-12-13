package org.study.jqboot.domain.board.entity;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Reply {
    private Integer no;
    private String writer;
    private String password;
    private String memo;
    private LocalDateTime wdate;
    private Integer refNo;
}