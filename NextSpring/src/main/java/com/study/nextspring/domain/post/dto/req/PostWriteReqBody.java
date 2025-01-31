package com.study.nextspring.domain.post.dto.req;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PostWriteReqBody(
        @NotBlank
        @Length(min = 2, max = 100)
        String title,

        @NotBlank
        @Length(min = 2, max = 10000000)
        String content,

        boolean published,
        boolean listed
) {}
