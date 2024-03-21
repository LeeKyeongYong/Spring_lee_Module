package com.surl.studyurl.domain.surl.data;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public record SurCreateReqBody (@NotBlank String url, String title){

}
