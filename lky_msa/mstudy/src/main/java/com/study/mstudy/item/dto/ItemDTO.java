package com.study.mstudy.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemDTO {
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    private String id;
    private String name;
    private String description;
    private long count;
    private String regDts;
    private String updDts;
}
