package com.surl.studyurl.domain.surl.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SurCreateReqBody {
    @NotBlank
    public String url;
    public String title;
}
