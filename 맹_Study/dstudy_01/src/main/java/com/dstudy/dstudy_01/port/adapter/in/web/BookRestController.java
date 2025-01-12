package com.dstudy.dstudy_01.port.adapter.in.web;

import com.dstudy.dstudy_01.port.application.in.BookUseCase;
import com.dstudy.dstudy_01.port.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpHeaders;
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

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadBook(@PathVariable(name = "id") Long id) {
        Book book = bookUseCase.getBook(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(book.getFileName(), StandardCharsets.UTF_8)
                .build());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(book.getContent());
    }
}
