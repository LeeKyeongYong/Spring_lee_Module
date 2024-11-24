package com.krstudy.kapi.domain.chat.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
data class ChatRoom(

    @Column(name = "room_name")
    val roomName: String? = null

) : BaseEntity() {}