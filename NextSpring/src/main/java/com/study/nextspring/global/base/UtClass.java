package com.study.nextspring.global.base;

import com.study.nextspring.global.app.AppConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class UtClass {
    // 문자열 관련 유틸리티
    public static class str {
        public static boolean isBlank(String str) {
            return str == null || str.trim().isEmpty();
        }

        public static boolean hasLength(String str) {
            return !isBlank(str);
        }
    }

    public static class jwt {
        public static String toString(String secret, long expireSeconds, Map<String, Object> body) {
            Date issuedAt = new Date();
            Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            String jwt = Jwts.builder()
                    .claims(body)
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .signWith(secretKey)
                    .compact();

            return jwt;
        }

        public static boolean isValid(String secret, String jwtStr) {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            try {
                Jwts
                        .parser()
                        .verifyWith(secretKey)
                        .build()
                        .parse(jwtStr);
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        public static Map<String, Object> payload(String secret, String jwtStr) {
            SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

            try {
                return (Map<String, Object>) Jwts
                        .parser()
                        .verifyWith(secretKey)
                        .build()
                        .parse(jwtStr)
                        .getPayload();
            } catch (Exception e) {
                return null;
            }
        }
    }

    // JSON 관련 유틸리티
    public static class json {
        @SneakyThrows
        public static String toString(Object obj) {
            return AppConfig.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }
    }

    // 스레드 관련 유틸리티
    public static class thread {
        @SneakyThrows
        public static void sleep(long millis) {
            Thread.sleep(millis);
        }
    }

    // 명령어 실행 관련 유틸리티
    public static class cmd {
        public static void runAsync(String cmd) {
            new Thread(() -> {
                run(cmd);
            }).start();
        }

        public static void run(String cmd) {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", cmd);
                Process process = processBuilder.start();
                process.waitFor(1, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // URL 관련 유틸리티
    public static class url {
        public static String modifyQueryParam(String url, String paramName, String paramValue) {
            url = deleteQueryParam(url, paramName);
            url = addQueryParam(url, paramName, paramValue);
            return url;
        }

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (!url.contains("?")) {
                url += "?";
            }

            if (!url.endsWith("?") && !url.endsWith("&")) {
                url += "&";
            }

            url += paramName + "=" + paramValue;
            return url;
        }

        public static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");
            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }

            String urlAfter = url.substring(startPoint + endPoint + 1);
            return url.substring(0, startPoint) + urlAfter;
        }

        public static String encode(String url) {
            return URLEncoder.encode(url, StandardCharsets.UTF_8);
        }
    }

    // 날짜 관련 유틸리티
    public static class date {
        public static String getCurrentDateFormatted(String pattern) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(new Date());
        }
    }

    // 파일 관련 유틸리티
    public static class file {

        private static final String ORIGIN_FILE_NAME_SEPARATOR = "--originFileName_";

        // Private constructor to prevent instantiation
        private file() {
        }

        public static void downloadByHttp(String url, String dirPath) {
            try {
                HttpClient client = HttpClient.newBuilder()
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();

                // First, send a HEAD request to check headers
                HttpResponse<Void> headResponse = client.send(
                        HttpRequest.newBuilder(URI.create(url))
                                .method("HEAD", HttpRequest.BodyPublishers.noBody())
                                .build(),
                        HttpResponse.BodyHandlers.discarding()
                );

                // Actual file download
                HttpResponse<Path> response = client.send(request,
                        HttpResponse.BodyHandlers.ofFile(
                                createTargetPath(url, dirPath, headResponse)
                        ));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException("Download error: " + e.getMessage(), e);
            }
        }

        private static Path createTargetPath(String url, String dirPath, HttpResponse<?> response) {
            // 디렉토리가 없으면 생성
            Path directory = Path.of(dirPath);
            if (!Files.exists(directory)) {
                try {
                    Files.createDirectories(directory);
                } catch (IOException e) {
                    throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
                }
            }

            // 파일명 생성
            String filename = getFilenameFromUrl(url);
            String extension = getExtensionFromResponse(response);

            return directory.resolve(filename + extension);
        }

        private static String getFilenameFromUrl(String url) {
            try {
                String path = new URI(url).getPath();
                String filename = Path.of(path).getFileName().toString();
                // 확장자 제거
                return filename.contains(".")
                        ? filename.substring(0, filename.lastIndexOf('.'))
                        : filename;
            } catch (URISyntaxException e) {
                // URL에서 파일명을 추출할 수 없는 경우 타임스탬프 사용
                return "download_" + System.currentTimeMillis();
            }
        }

        private static String getExtensionFromResponse(HttpResponse<?> response) {
            return response.headers()
                    .firstValue("Content-Type")
                    .map(contentType -> {
                        // MIME 타입에 따른 확장자 매핑
                        return switch (contentType.split(";")[0].trim().toLowerCase()) {
                            case "application/json" -> ".json";
                            case "text/plain" -> ".txt";
                            case "text/html" -> ".html";
                            case "image/jpeg" -> ".jpg";
                            case "image/png" -> ".png";
                            case "application/pdf" -> ".pdf";
                            case "application/xml" -> ".xml";
                            case "application/zip" -> ".zip";
                            default -> "";
                        };
                    })
                    .orElse("");
        }

        public static String getOriginFileName(String file) {
            if (file.contains(ORIGIN_FILE_NAME_SEPARATOR)) {
                String[] fileInfos = file.split(ORIGIN_FILE_NAME_SEPARATOR);
                return fileInfos[fileInfos.length - 1];
            }
            return Paths.get(file).getFileName().toString();
        }

        public static String toFile(MultipartFile multipartFile, String tempDirPath) {
            if (multipartFile == null || multipartFile.isEmpty()) return "";

            String filePath = tempDirPath + "/" + UUID.randomUUID() + ORIGIN_FILE_NAME_SEPARATOR + multipartFile.getOriginalFilename();

            try {
                multipartFile.transferTo(new File(filePath));
            } catch (IOException e) {
                return "";
            }

            return filePath;
        }

        public static void moveFile(String filePath, File file) {
            moveFile(filePath, file.getAbsolutePath());
        }

        public static boolean exists(String file) {
            return new File(file).exists();
        }

        public static boolean exists(MultipartFile file) {
            return file != null && !file.isEmpty();
        }

        public static String tempCopy(String file) {
            String tempPath = AppConfig.getTempDirPath() + "/" + getFileName(file);
            copy(file, tempPath);
            return tempPath;
        }

        private static String getFileName(String file) {
            return Paths.get(file).getFileName().toString();
        }

        private static void copy(String file, String tempDirPath) {
            try {
                Files.copy(Paths.get(file), Paths.get(tempDirPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static class DownloadFileFailException extends RuntimeException {
        }

        private static String getFileExt(File file) {
            Tika tika = new Tika();
            String mimeType = "";

            try {
                mimeType = tika.detect(file);
            } catch (IOException e) {
                return null;
            }

            String ext = mimeType.replace("image/", "");
            ext = ext.replace("jpeg", "jpg");
            return ext.toLowerCase();
        }

        public static String getFileExt(String fileName) {
            int pos = fileName.lastIndexOf(".");
            if (pos == -1) {
                return "";
            }
            return fileName.substring(pos + 1).trim();
        }

        public static String getFileNameFromUrl(String fileUrl) {
            try {
                return Paths.get(new URI(fileUrl).getPath()).getFileName().toString();
            } catch (URISyntaxException e) {
                return "";
            }
        }

        public static String downloadFileByHttp(String fileUrl, String outputDir) {
            String originFileName = getFileNameFromUrl(fileUrl);
            String fileExt = getFileExt(originFileName);

            if (fileExt.isEmpty()) {
                fileExt = "tmp";
            }

            String tempFileName = UUID.randomUUID() + ORIGIN_FILE_NAME_SEPARATOR + originFileName + "." + fileExt;
            String filePath = outputDir + "/" + tempFileName;

            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URI(fileUrl).toURL().openStream());
                FileChannel fileChannel = fileOutputStream.getChannel();
                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            } catch (Exception e) {
                throw new DownloadFileFailException();
            }

            File file = new File(filePath);

            if (file.length() == 0) {
                throw new DownloadFileFailException();
            }

            if (fileExt.equals("tmp")) {
                String ext = getFileExt(file);

                if (ext == null || ext.isEmpty()) {
                    throw new DownloadFileFailException();
                }

                String newFilePath = filePath.replace(".tmp", "." + ext);
                moveFile(filePath, newFilePath);
                filePath = newFilePath;
            }

            return filePath;
        }

        public static void moveFile(String filePath, String destFilePath) {
            Path file = Paths.get(filePath);
            Path destFile = Paths.get(destFilePath);

            try {
                Files.move(file, destFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ignored) {
            }
        }

        public static String getExt(String filename) {
            return Optional.ofNullable(filename)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(filename.lastIndexOf(".") + 1).toLowerCase())
                    .orElse("");
        }

        public static String getFileExtTypeCodeFromFileExt(String ext) {
            return switch (ext) {
                case "jpeg", "jpg", "gif", "png" -> "img";
                case "mp4", "avi", "mov" -> "video";
                case "mp3" -> "audio";
                default -> "etc";
            };
        }

        public static String getFileExtType2CodeFromFileExt(String ext) {
            return switch (ext) {
                case "jpeg", "jpg" -> "jpg";
                case "gif", "png", "mp4", "mov", "avi", "mp3" -> ext;
                default -> "etc";
            };
        }

        public static void remove(String filePath) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
