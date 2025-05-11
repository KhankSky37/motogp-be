package com.example.motogp_b.service;

import com.example.motogp_b.dto.NewsArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsArticleService {
    List<NewsArticleDto> findAll();

    NewsArticleDto findById(String id);

    NewsArticleDto create(NewsArticleDto newsArticleDto, MultipartFile photoFile);

    NewsArticleDto update(String id, NewsArticleDto newsArticleDto, MultipartFile photoFile);

    void deleteById(String id);
}