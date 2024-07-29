package com.react.meetup.domain.service;

import com.react.meetup.domain.entity.Meetup;
import com.react.meetup.domain.repository.MeetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetupService {

    @Autowired
    private MeetupRepository meetupRepository;

    public List<Meetup> getAllMeetups() {
        return meetupRepository.findAll();
    }

    public Meetup saveMeetup(Meetup meetup) {
        return meetupRepository.save(meetup);
    }

    public void deleteMeetup(Long id) {
        meetupRepository.deleteById(id);
    }

    public Meetup getMeetupById(Long id) {
        return meetupRepository.findById(id).orElse(null);
    }
}