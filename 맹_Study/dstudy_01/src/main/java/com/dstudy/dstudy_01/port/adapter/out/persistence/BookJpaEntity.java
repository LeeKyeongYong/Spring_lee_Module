package com.dstudy.dstudy_01.port.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import com.dstudy.dstudy_01.port.domain.Book;

@Entity
@Table(name = "books")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class BookJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String fileName;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "page_num")
    private int pageNum;

    @Lob
    private byte[] content;

    public static BookJpaEntity fromDomain(Book book) {
        return BookJpaEntity.builder()
                .id(book.getId())
                .title(book.getTitle())
                .fileName(book.getFileName())
                .uploadDate(book.getUploadDate())
                .pageNum(book.getPageNum())
                .content(book.getContent())
                .build();
    }

    public Book toDomain() {
        return Book.builder()
                .id(id)
                .title(title)
                .fileName(fileName)
                .uploadDate(uploadDate)
                .pageNum(pageNum)
                .content(content)
                .build();
    }
}