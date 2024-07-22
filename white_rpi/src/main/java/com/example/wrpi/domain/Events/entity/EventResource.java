package com.example.wrpi.domain.Events.entity;


import com.example.wrpi.domain.Events.controller.EventController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventResource extends EntityModel<Event> {

    public EventResource(Event event, Link... links) {
        super(event, List.of(links));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
        add(linkTo(EventController.class).withRel("query-events"));
        add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        // Add the 'id' field explicitly if needed
        add(Link.of("/api/events/" + event.getId()).withRel("id"));
    }
}
