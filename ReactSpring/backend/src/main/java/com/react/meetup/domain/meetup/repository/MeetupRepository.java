package com.react.meetup.domain.meetup.repository;

import com.react.meetup.domain.meetup.entity.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {
}