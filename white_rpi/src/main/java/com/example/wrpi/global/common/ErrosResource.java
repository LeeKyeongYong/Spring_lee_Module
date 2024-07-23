package com.example.wrpi.global.common;

import com.example.wrpi.domain.home.HomeController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrosResource extends EntityModel<Errors> {
    public ErrosResource (Errors content, Link... links) {
        super(content, links);
        add(linkTo(methodOn(HomeController.class).index()).withRel("index"));
    }
}