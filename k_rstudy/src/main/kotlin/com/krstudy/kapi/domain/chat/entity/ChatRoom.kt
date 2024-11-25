package com.krstudy.kapi.domain.chat.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.krstudy.kapi.domain.member.entity.Member
import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
data class ChatRoom(

    @Column(name = "room_name")
    val roomName: String? = null,

    @ManyToOne(fetch = FetchType.EAGER)
    var author: Member? = null,

    ) : BaseEntity() {}