package com.study.nextspring.domain.post.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostWriteItemReqBody {
    @NotBlank public String title;
    @NotBlank public String body;
    @NotNull boolean published;
    @NotNull boolean listed;
}