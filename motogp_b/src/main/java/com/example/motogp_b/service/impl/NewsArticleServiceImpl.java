package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.NewsArticleDto;
import com.example.motogp_b.entity.NewsArticle;
import com.example.motogp_b.repository.NewsArticleRepository;
import com.example.motogp_b.service.FileStorageService;
import com.example.motogp_b.service.NewsArticleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsArticleServiceImpl implements NewsArticleService {
    NewsArticleRepository newsArticleRepository;
    ModelMapper modelMapper;
    FileStorageService fileStorageService;

    private static final String NEWSARTICLE_SUBDIRECTORY = "news";
    @Override
    public List<NewsArticleDto> findAll() {
        return newsArticleRepository.findAll().stream()
                .map(article -> modelMapper.map(article, NewsArticleDto.class))
                .toList();
    }

    @Override
    public NewsArticleDto findById(String id) {
        return modelMapper.map(newsArticleRepository.findById(id), NewsArticleDto.class);
    }

    @Override
    public NewsArticleDto create(NewsArticleDto newsArticleDto, MultipartFile photoFile) {
        NewsArticle newsArticle = modelMapper.map(newsArticleDto, NewsArticle.class);

        try {
            String photoUrl = fileStorageService.storeFile(photoFile, NEWSARTICLE_SUBDIRECTORY);
            newsArticle.setImageUrl(photoUrl);
        } catch (IOException e) {
            throw new RuntimeException("Could not process photo file for news article: " + newsArticleDto.getTitle(), e);
        }
        return modelMapper.map(newsArticleRepository.save(newsArticle), NewsArticleDto.class);
    }

    @Override
    @Transactional
    public NewsArticleDto update(String id, NewsArticleDto newsArticleDto, MultipartFile photoFile) {
        NewsArticle existingArticle = newsArticleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News article not found with ID: " + id));

        String newArticleId = newsArticleDto.getId();
        if (!Objects.equals(id, newArticleId) && newsArticleRepository.existsNewsArticleById(newArticleId)) {
            throw new RuntimeException("New NewsArticle ID '" + newArticleId + "' already exists. Please use a different ID.");
        }

        String oldImageUrl = existingArticle.getImageUrl();
        String newImageUrl = oldImageUrl;

        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                if (StringUtils.hasText(oldImageUrl)) {
                    try {
                        fileStorageService.deleteFile(oldImageUrl);
                    } catch (IOException e) {
                        // Log the error but don't rethrow, allow the update process to continue
                        System.err.println("WARN: Could not delete old image file: " + oldImageUrl + " - " + e.getMessage());
                    }
                }
                newImageUrl = fileStorageService.storeFile(photoFile, NEWSARTICLE_SUBDIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Could not store new image file for news article: " + newArticleId, e);
            }
        }

        modelMapper.map(newsArticleDto, existingArticle);
        existingArticle.setId(newArticleId); // Or ensure DTO mapping handles this if ID is part of DTO general mapping
        existingArticle.setImageUrl(newImageUrl); // Set the potentially updated image URL

        NewsArticle updatedArticle = newsArticleRepository.save(existingArticle);
        return modelMapper.map(updatedArticle, NewsArticleDto.class);
    }


    @Override
    public void deleteById(String id) {
        newsArticleRepository.findById(id).ifPresent(article -> {
            if (StringUtils.hasText(article.getImageUrl())) {
                try {
                    fileStorageService.deleteFile(article.getImageUrl());
                } catch (IOException e) {
                    System.err.println("Could not delete image file: " + article.getImageUrl() + " for news article ID: " + id + " - " + e.getMessage());
                }
            }
            newsArticleRepository.deleteById(id);
        });
    }

}