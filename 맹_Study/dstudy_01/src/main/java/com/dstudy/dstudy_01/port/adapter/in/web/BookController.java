package com.dstudy.dstudy_01.port.adapter.in.web;

import com.dstudy.dstudy_01.global.https.ReqData;
import com.dstudy.dstudy_01.report.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final ReqData rq;

    @GetMapping("list")
    public String listEmployees() {
        //rq.setAttribute("employees", employees);
        return "domain/book/list";
    }
    @GetMapping("/{id}")
    public String listEmployees(@PathVariable Long id) {
        //rq.setAttribute("employees", employees);
        return "domain/book/view";
    }



}
