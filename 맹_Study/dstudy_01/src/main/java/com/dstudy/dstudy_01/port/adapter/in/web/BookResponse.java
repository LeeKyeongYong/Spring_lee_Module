package com.dstudy.dstudy_01.port.adapter.in.web;

import com.dstudy.dstudy_01.port.domain.Book;
import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookResponse {
    Long id;
    String title;
    String fileName;
    LocalDateTime uploadDate;
    int pageNum;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .fileName(book.getFileName())
                .uploadDate(book.getUploadDate())
                .pageNum(book.getPageNum())
                .build();
    }
}