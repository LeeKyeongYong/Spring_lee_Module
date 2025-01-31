package com.study.nextspring.domain.post.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PostCommentModifyReqBody(
        @NotBlank
        @Length(min = 2, max = 100)
        String content
) {
}