package com.fly.clstudy.global.exceptions;

import com.fly.clstudy.global.https.ReqData;
import com.fly.clstudy.global.https.RespData;
import com.fly.clstudy.global.jpa.util.EmpClass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final ReqData rq;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("resultCode", "500-1");
        body.put("statusCode", 500);
        body.put("msg", ex.getLocalizedMessage());

        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        body.put("data", data);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        data.put("trace", sw.toString().replace("\t", "    ").split("\\r\\n"));

        String path = rq.getCurrentUrlPath();
        data.put("path", path);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }


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
