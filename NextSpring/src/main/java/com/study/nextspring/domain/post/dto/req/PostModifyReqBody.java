package com.study.nextspring.domain.post.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public record PostModifyReqBody(
        @NotBlank
        @Length(min = 2, max = 100)
        String title,

        @NotBlank
        @Length(min = 2, max = 10000000)
        String content,

        boolean published,
        boolean listed
) {}