package com.dstudy.dstudy_01.port.adapter.out.persistence;

import com.dstudy.dstudy_01.global.exception.BookNotFoundException;
import com.dstudy.dstudy_01.port.application.out.BookPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import com.dstudy.dstudy_01.port.domain.Book;

@Repository
@RequiredArgsConstructor
class BookPersistenceAdapter implements BookPort {
    private final BookRepository bookRepository;

    @Override
    public List<Book> findAllByPage(int page) {
        Pageable pageable = PageRequest.of(page - 1, 15);
        return bookRepository.findAll(pageable)
                .stream()
                .map(BookJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .map(BookJpaEntity::toDomain)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book save(Book book) {
        BookJpaEntity entity = BookJpaEntity.fromDomain(book);
        return bookRepository.save(entity).toDomain();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
