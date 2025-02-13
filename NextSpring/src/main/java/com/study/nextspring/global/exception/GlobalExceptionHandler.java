package com.study.nextspring.global.exception;
import com.study.nextspring.global.app.AppConfig;
import com.study.nextspring.global.httpsdata.RespData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RespData<Void>> handle(NoSuchElementException ex) {

        if (AppConfig.isNotProd()) ex.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(RespData.of("404-1", "해당 데이터가 존재하지 않습니다.", null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RespData<Void>> handle(MethodArgumentNotValidException ex) {

        if (AppConfig.isNotProd()) ex.printStackTrace();

        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
                .sorted(Comparator.comparing(String::toString))
                .collect(Collectors.joining("\n"));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(RespData.of("400-1", message, null));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RespData<Void>> handle(ServiceException ex) {

        if (AppConfig.isNotProd()) ex.printStackTrace();

        RespData<Void> rsData = ex.getRsData();

        return ResponseEntity
                .status(rsData.getStatusCode())
                .body(rsData);
    }
}