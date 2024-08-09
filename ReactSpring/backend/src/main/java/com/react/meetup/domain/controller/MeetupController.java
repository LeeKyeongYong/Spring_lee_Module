package com.react.meetup.domain.controller;

import com.react.meetup.domain.entity.Meetup;
import com.react.meetup.domain.service.MeetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/meetups")
public class MeetupController {

    @Autowired
    private MeetupService meetupService;

    @GetMapping("/list")
    public List<Meetup> getAllMeetups() {
        return meetupService.getAllMeetups();
    }

    @PostMapping("/addMeetup")
    public Meetup addMeetup(@RequestBody Meetup meetup) {
        System.out.println("addMeetupHandler: "+meetup.toString());
        return meetupService.saveMeetup(meetup);
    }

    @DeleteMapping("/{id}")
    public void deleteMeetup(@PathVariable Long id) {
        meetupService.deleteMeetup(id);
    }
}