package com.dstudy.dstudy_01.port.application.out;

import com.dstudy.dstudy_01.port.domain.Book;
import java.util.List;

public interface BookPort {
    List<Book> findAllByPage(int page);
    Book findById(Long id);
    Book save(Book book);
    void deleteById(Long id);
}