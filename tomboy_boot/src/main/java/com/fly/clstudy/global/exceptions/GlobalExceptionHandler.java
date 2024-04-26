package com.fly.clstudy.global.exceptions;

import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.dto.EmpClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.geom.RectangularShape;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    @ResponseBody
    public ResponseEntity<RespData<EmpClass>> handleException(GlobalException ex) {

        RespData<EmpClass> rsData = ex.getRsData();

        return ResponseEntity.status(rsData.getStatusCode()).body(rsData);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<RespData<EmpClass>> handleException(MethodArgumentNotValidException ex) {
        String resultCode = "400-" + ex.getBindingResult().getFieldError().getCode();
        String msg = ex.getBindingResult().getFieldError().getField() + " : " + ex.getBindingResult().getFieldError().getDefaultMessage();

        return handleException(
                new GlobalException(
                        resultCode,
                        msg
                )
        );
    }
}
