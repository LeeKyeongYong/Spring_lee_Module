package com.surl.studyurl.domain.surl.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SurCreateReqBody {
    public String url;
    @NotBlank
    public String title;
}
