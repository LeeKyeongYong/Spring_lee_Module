package com.react.meetup.domain.repository;

import com.react.meetup.domain.entity.Meetup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetupRepository extends JpaRepository<Meetup, Long> {
}