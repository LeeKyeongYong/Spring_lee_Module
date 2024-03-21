package com.surl.studyurl.domain.surl.data;

import jakarta.validation.constraints.NotBlank;


public record SurCreateReqBody (@NotBlank String url, String title){

}
