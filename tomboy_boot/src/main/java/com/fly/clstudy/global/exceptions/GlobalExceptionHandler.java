package com.fly.clstudy.global.exceptions;

import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.dto.EmpClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        log.debug("handleException started!");
        RespData<EmpClass> rsData = ex.getRsData();

        return ResponseEntity.status(rsData.getStatusCode()).body(rsData);
    }
}
