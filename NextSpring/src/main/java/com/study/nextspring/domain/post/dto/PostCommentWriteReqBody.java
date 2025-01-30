package com.study.nextspring.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PostCommentWriteReqBody( // public 추가
                                       @NotBlank
                                       @Length(min = 2, max = 100)
                                       String content
) {
}