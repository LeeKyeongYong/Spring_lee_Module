package com.mstudy.historyservice.history.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResponseDTO {
    private String code;
    private String message;
}