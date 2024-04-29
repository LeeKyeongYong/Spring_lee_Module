package com.fly.clstudy.sur.data;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public static class SurlAddReqBody {
    @NotBlank
    private String body;
    @NotBlank
    private String url;
}
}