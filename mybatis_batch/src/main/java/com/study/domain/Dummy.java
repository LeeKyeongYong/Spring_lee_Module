package com.study.domain;

import java.util.Date;

public class Dummy {
    private Long no;
    private String message;
    private Date inputDate;

    public Dummy() {}

    public Dummy(Long no, String message, Date inputDate) {
        this.no = no;
        this.message = message;
        this.inputDate = inputDate;
    }

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getInputDate() {
        return inputDate;
    }

    public void setInputDate(Date inputDate) {
        this.inputDate = inputDate;
    }
}