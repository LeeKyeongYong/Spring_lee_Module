package com.fly.clstudy.domain.sur.data;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SurlAddReqBody {
    @NotBlank
    private String body;
    @NotBlank
    private String url;
}