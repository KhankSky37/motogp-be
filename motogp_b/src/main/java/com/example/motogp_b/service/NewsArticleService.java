package com.example.motogp_b.service;

import com.example.motogp_b.dto.NewsArticleDto;

import java.util.List;

public interface NewsArticleService {
    List<NewsArticleDto> findAll();

    NewsArticleDto findById(String id);

    NewsArticleDto create(NewsArticleDto newsArticleDto);

    NewsArticleDto update(String id, NewsArticleDto newsArticleDto);

    void deleteById(String id);
}