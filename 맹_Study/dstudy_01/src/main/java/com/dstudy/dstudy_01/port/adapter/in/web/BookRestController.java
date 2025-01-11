package com.dstudy.dstudy_01.port.adapter.in.web;

import com.dstudy.dstudy_01.port.application.in.BookUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {
    private final BookUseCase bookUseCase;

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(@RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(
                bookUseCase.getAllBooks(page).stream()
                        .map(BookResponse::from)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping
    public ResponseEntity<Void> uploadBook(
            @RequestParam String title,
            @RequestParam MultipartFile file) {
        bookUseCase.saveBook(title, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(BookResponse.from(bookUseCase.getBook(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookUseCase.removeBook(id);
        return ResponseEntity.ok().build();
    }
}
