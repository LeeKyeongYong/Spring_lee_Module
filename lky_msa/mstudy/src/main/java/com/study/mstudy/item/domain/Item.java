package com.study.mstudy.item.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name="ITEM")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @Column(name = "ID", length = 30)
    private String id;

    @Column(name = "NAME", length = 30)
    private String name;

    @Column(name = "DESCRIPTION", length = 30)
    private String description;

    @Column(name = "ITEM_TYPE", length = 1)
    private String itemType;

    @Column(name = "CNT", length = 10)
    private long count;

    @Column(name = "REG_DTS", length = 14)
    private String regDts;

    @Column(name = "UPD_DTS", length = 14)
    private String updDts;
}
