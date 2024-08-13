package com.study.mstudy.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemDTO {
    @Schema(description = "물품ID" , example = "TESTID")
    @NotBlank(message = "ID는 필수 입력 값입니다.")
    @Size(max=10, message = "ID는 크기 10이하까지 작성가능합니다.")
    private String id;

    @Schema(description = "물품명" , example = "과일")
    @Size(max=20, message = "이름은 20자까지 작성가능합니다.")
    private String name;

    @Schema(description = "물품설명" , example = "물품설명테스트")
    @Size(max=200, message = "설명은 최대 200자까지 작성가능합니다.")
    private String description;

    @Schema(description = "물품유형" , example = "F: 음식, C: 옷")
    private String itemType;

    @Schema(description = "물품개수" , example = "100")
    @Positive
    private long count;

    @Schema(hidden = true)
    private String regDts;

    @Schema(hidden = true)
    private String updDts;
}
