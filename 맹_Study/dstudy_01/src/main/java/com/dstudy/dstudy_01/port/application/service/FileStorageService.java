package com.dstudy.dstudy_01.port.application.service;

import com.dstudy.dstudy_01.global.exception.FileProcessingException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    private final Path rootLocation;
    private final Path documentPath;
    private final Path previewPath;

    public FileStorageService() {
        this.rootLocation = Paths.get("upload-dir");
        this.documentPath = rootLocation.resolve("documents");
        this.previewPath = rootLocation.resolve("previews");
        try {
            Files.createDirectories(documentPath);
            Files.createDirectories(previewPath);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage locations", e);
        }
    }

    public void saveDocument(Long bookId, byte[] content) throws IOException {
        Path documentFile = documentPath.resolve("document_" + bookId + ".pdf");
        Files.write(documentFile, content);
    }

    public void savePreviewImage(Long bookId, int pageNumber, BufferedImage image) throws IOException {
        Path previewFile = previewPath.resolve("preview_" + bookId + "_" + pageNumber + ".png");
        ImageIO.write(image, "png", previewFile.toFile());
    }

    public void deleteBookFiles(Long id) {
        try {
            // PDF 파일 삭제
            Files.deleteIfExists(documentPath.resolve("document_" + id + ".pdf"));

            // 미리보기 이미지들 삭제 (최대 10페이지)
            for (int i = 1; i <= 10; i++) {
                Files.deleteIfExists(previewPath.resolve("preview_" + id + "_" + i + ".png"));
            }
        } catch (IOException e) {
            throw new FileProcessingException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }
}