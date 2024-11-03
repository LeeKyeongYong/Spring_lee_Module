package com.krstudy.kapi.domain.uploads.entity

import com.krstudy.kapi.global.jpa.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "file_uploads")
class FileEntity(

    @Column(nullable = false)
    val originalFileName: String,

    @Column(nullable = false)
    val storedFileName: String,  // UUID 기반 파일명

    @Column(nullable = false)
    val filePath: String,

    @Column(nullable = false)
    val fileSize: Long,

    @Column(nullable = false)
    val fileType: String,

    @Column(nullable = false)
    val contentType: String,

    @Column(nullable = false)
    val uploadDateTime: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val uploadedBy: String,  // 업로더 식별자

    @Column
    val checksum: String? = null,  // 파일 무결성 검사용

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: FileStatus = FileStatus.ACTIVE
) : BaseEntity() {
    enum class FileStatus {
        ACTIVE, DELETED
    }
}