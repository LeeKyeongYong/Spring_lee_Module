package com.krstudy.kapi.domain.uploads.repository

import com.krstudy.kapi.domain.uploads.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : JpaRepository<FileEntity, Long> {
    fun findByStoredFileName(storedFileName: String): FileEntity?

    @Query("SELECT f FROM FileEntity f WHERE f.uploadedBy = :uploadedBy AND f.status = 'ACTIVE'")
    fun findAllByUploadedBy(uploadedBy: String): List<FileEntity>
}
