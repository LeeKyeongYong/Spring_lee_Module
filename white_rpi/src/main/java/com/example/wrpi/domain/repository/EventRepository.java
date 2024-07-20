package com.example.wrpi.domain.repository;

import com.example.wrpi.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Integer> { }
