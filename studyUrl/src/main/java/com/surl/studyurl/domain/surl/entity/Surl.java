package com.surl.studyurl.domain.surl.entity;

import com.surl.studyurl.global.jpa.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;

@Entity
public class Surl extends BaseTime {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String url;
    private String title;
}
