package com.jqstudy.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Locale;

@Controller
public class EmployeeController {

    @GetMapping({"/", "/index"})
    public String index(Locale locale, Model model) {
        return "index";
    }
}
