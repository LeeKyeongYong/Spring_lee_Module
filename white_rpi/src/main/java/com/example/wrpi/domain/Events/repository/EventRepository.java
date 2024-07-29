package com.example.wrpi.domain.Events.repository;

import com.example.wrpi.domain.Events.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
