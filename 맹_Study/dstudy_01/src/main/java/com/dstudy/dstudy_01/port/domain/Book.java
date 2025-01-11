package com.dstudy.dstudy_01.port.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // PRIVATE로 변경
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    private Long id;
    private String title;
    private String fileName;
    private LocalDateTime uploadDate;
    private int pageNum;
    private byte[] content;

    // 정적 팩토리 메서드 추가
    public static Book createBook(String title, String fileName, int pageNum, byte[] content) {
        return Book.builder()
                .title(title)
                .fileName(fileName)
                .pageNum(pageNum)
                .uploadDate(LocalDateTime.now())
                .content(content)
                .build();
    }
}