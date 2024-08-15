package com.mstudy.serverstudy.authentication.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {
    private String code;
    private String message;
    private String token;
}