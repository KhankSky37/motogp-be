package com.example.motogp_b.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeFile(MultipartFile file, String subDirectory) throws IOException;

    void deleteFile(String fileUrl) throws IOException;
}