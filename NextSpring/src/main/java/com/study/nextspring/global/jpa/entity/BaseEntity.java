package com.study.nextspring.global.jpa.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY) // AUTO_INCREMENT
    @Setter(AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    private Long id;
}