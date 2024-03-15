package com.surl.studyurl.domain.surl.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SurModifyReqBody {
    @NotBlank
    public String title;
}
