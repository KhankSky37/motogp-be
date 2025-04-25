package com.example.motogp_b.service.impl;

import com.example.motogp_b.service.FileStorageService;
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

    private static final String BASE_UPLOAD_DIR = "static/uploads";
    private static final Path BASE_UPLOAD_PATH = Paths.get(BASE_UPLOAD_DIR);

    @Override
    public String storeFile(MultipartFile file, String subDirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path entityUploadPath = BASE_UPLOAD_PATH.resolve(subDirectory);
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

            String webPathPrefix = BASE_UPLOAD_DIR.substring(BASE_UPLOAD_DIR.indexOf('/') + 1).replace("\\", "/");
            return "/" + webPathPrefix + "/" + subDirectory + "/" + uniqueFilename;

        } catch (IOException e) {
            System.err.println("Could not save file: " + originalFilename + " in directory: " + subDirectory);
            throw new IOException("Could not save file: " + originalFilename, e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        try {
            String relativePath = fileUrl.startsWith("/") ? fileUrl.substring(1) : fileUrl;
            Path filePath = Paths.get("src/main/resources/" + relativePath);

            if (filePath.normalize().startsWith(Paths.get("src/main/resources/" + BASE_UPLOAD_DIR).normalize())) {
                Files.deleteIfExists(filePath);
            } else {
                System.err.println("Attempted to delete file outside of the designated upload directory: " + fileUrl);
            }
        } catch (IOException e) {
            System.err.println("Could not delete file: " + fileUrl);
        }
    }
}