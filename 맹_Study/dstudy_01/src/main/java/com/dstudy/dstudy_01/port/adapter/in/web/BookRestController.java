package com.dstudy.dstudy_01.port.adapter.in.web;

import com.dstudy.dstudy_01.port.application.in.BookUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadBook(
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {
        try {
            bookUseCase.saveBook(title, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookUseCase.removeBook(id);
        return ResponseEntity.ok().build();
    }
}
