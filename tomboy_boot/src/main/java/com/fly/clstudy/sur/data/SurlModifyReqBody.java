package com.fly.clstudy.sur.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class SurlModifyReqBody {
    @NotBlank
    private String body;
    @NotBlank
    private String url;
}
