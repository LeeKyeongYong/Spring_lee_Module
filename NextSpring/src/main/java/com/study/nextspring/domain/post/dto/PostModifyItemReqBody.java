package com.study.nextspring.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostModifyItemReqBody {
    @NotBlank
    public String title;

    @NotBlank
    public String body;

    @NotNull
    boolean published;

    @NotNull
    boolean listed;

    // boolean 필드에 대한 getter 메서드 추가
    public boolean isPublished() {
        return published;
    }

    public boolean isListed() {
        return listed;
    }
}
