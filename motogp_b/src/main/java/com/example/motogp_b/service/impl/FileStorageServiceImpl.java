package com.example.motogp_b.service.impl;

import com.example.motogp_b.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String baseUploadDir;
    private final Path baseUploadPath;

    public FileStorageServiceImpl(@Value("${upload.dir:static/uploads}") String uploadDir) {
        this.baseUploadDir = uploadDir.endsWith("/") ? uploadDir.substring(0, uploadDir.length() - 1) : uploadDir;
        this.baseUploadPath = Paths.get(this.baseUploadDir);

        // Tạo thư mục nếu chưa tồn tại
        try {
            if (!Files.exists(this.baseUploadPath)) {
                Files.createDirectories(this.baseUploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Không thể khởi tạo thư mục lưu trữ", e);
        }
    }

    @Override
    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path entityUploadPath = baseUploadPath.resolve(subDirectory);
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        int lastDotIndex = originalFilename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            fileExtension = originalFilename.substring(lastDotIndex);
        }

        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path filePath = entityUploadPath.resolve(uniqueFilename);

        try {
            if (!Files.exists(entityUploadPath)) {
                Files.createDirectories(entityUploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Trả về đường dẫn web nhất quán với cơ sở dữ liệu
            return "/uploads/" + subDirectory + "/" + uniqueFilename;

        } catch (IOException e) {
            System.err.println("Không thể lưu file: " + originalFilename + " trong thư mục: " + subDirectory);
            throw new IOException("Không thể lưu file: " + originalFilename, e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            // Trích xuất đường dẫn tương đối sau /uploads/
            String relativePath = fileUrl;
            if (fileUrl.startsWith("/uploads/")) {
                relativePath = fileUrl.substring("/uploads/".length());
            } else if (fileUrl.startsWith("/")) {
                relativePath = fileUrl.substring(1);
            }

            Path filePath = baseUploadPath.resolve(relativePath);

            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                System.err.println("Không tìm thấy file: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Không thể xóa file: " + fileUrl);
            throw e;
        }
    }
}