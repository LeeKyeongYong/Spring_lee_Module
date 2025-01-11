package com.dstudy.dstudy_01.port.application.service;

import com.dstudy.dstudy_01.global.exception.FileProcessingException;
import com.dstudy.dstudy_01.global.exception.InvalidFileTypeException;
import com.dstudy.dstudy_01.port.application.in.BookUseCase;
import com.dstudy.dstudy_01.port.application.out.BookPort;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.dstudy.dstudy_01.port.domain.Book;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService implements BookUseCase {
    private final BookPort bookPort;
    private final FileStorageService fileStorageService;

    @Override
    public List<Book> getAllBooks(int page) {
        return bookPort.findAllByPage(page);
    }

    @Override
    public Book getBook(Long id) {
        return bookPort.findById(id);
    }

    @Override
    public void saveBook(String title, MultipartFile file) {
        if (!isPdfFile(file)) {
            throw new InvalidFileTypeException("PDF 파일만 업로드 가능합니다.");
        }
        try {
            PDDocument document = PDDocument.load(file.getInputStream());
            int totalPages = document.getNumberOfPages();

            Book book = Book.builder()
                    .title(title)
                    .fileName(file.getOriginalFilename())
                    .pageNum(totalPages)
                    .uploadDate(LocalDateTime.now())
                    .content(file.getBytes())
                    .build();

            Book savedBook = bookPort.save(book);  // 저장된 Book 객체 받기
            fileStorageService.saveDocument(savedBook.getId(), file.getBytes());
            generatePreviewImages(document, savedBook.getId());
            document.close();
        } catch (IOException e) {
            throw new FileProcessingException("파일 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public void removeBook(Long id) {
        bookPort.deleteById(id);
        fileStorageService.deleteBookFiles(id);
    }

    private boolean isPdfFile(MultipartFile file) {
        return file.getContentType() != null &&
                file.getContentType().equals("application/pdf");
    }

    private void generatePreviewImages(PDDocument document, Long bookId) throws IOException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        int maxPages = Math.min(10, document.getNumberOfPages());

        for (int i = 0; i < maxPages; i++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(i, 150);
            fileStorageService.savePreviewImage(bookId, i + 1, image);
        }
    }
}