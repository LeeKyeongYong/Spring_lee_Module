package com.dstudy.dstudy_01.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "billing_details")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "billing_type", discriminatorType = DiscriminatorType.STRING)
@Getter @Setter @NoArgsConstructor @ToString
public abstract class BillingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner")
    private String owner;

    protected BillingDetails(Long id, String owner) {
        this.id = id;
        this.owner = owner;
    }
}