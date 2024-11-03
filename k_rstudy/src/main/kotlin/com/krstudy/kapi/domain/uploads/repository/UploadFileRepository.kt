package com.krstudy.kapi.domain.uploads.repository

import com.krstudy.kapi.domain.uploads.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UploadFileRepository : JpaRepository<FileEntity, Long> {
    // JPQL query를 명시적으로 정의
    @Query("SELECT f FROM StoredFile f WHERE f.uploadedBy = :uploadedBy AND f.status = 'ACTIVE'")
    fun findAllByUploadedBy(@Param("uploadedBy") uploadedBy: String): List<FileEntity>

    // 추가적인 쿼리 메서드들
    fun findByStoredFileNameAndStatus(storedFileName: String, status: FileEntity.FileStatus): FileEntity?

    @Query("SELECT f FROM StoredFile f WHERE f.status = 'ACTIVE' AND f.id = :id")
    fun findActiveFileById(@Param("id") id: Long): FileEntity?

    @Query("SELECT COUNT(f) > 0 FROM StoredFile f WHERE f.checksum = :checksum AND f.status = 'ACTIVE'")
    fun existsByChecksumAndStatus(@Param("checksum") checksum: String): Boolean

    @Query("UPDATE StoredFile f SET f.status = 'DELETED' WHERE f.id = :id AND f.uploadedBy = :uploadedBy")
    fun softDeleteFile(@Param("id") id: Long, @Param("uploadedBy") uploadedBy: String)
}