package com.dstudy.dstudy_01.port.application.in;

import org.springframework.web.multipart.MultipartFile;
import com.dstudy.dstudy_01.port.domain.Book;
import java.util.List;

public interface BookUseCase {
    List<Book> getAllBooks(int page);
    Book getBook(Long id);
    void saveBook(String title, MultipartFile file);
    void removeBook(Long id);
}