package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.NewsArticleDto;
import com.example.motogp_b.entity.NewsArticle;
import com.example.motogp_b.repository.NewsArticleRepository;
import com.example.motogp_b.service.FileStorageService;
import com.example.motogp_b.service.NewsArticleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public NewsArticleDto update(String id, NewsArticleDto newsArticleDto) {
        NewsArticle newsArticle = modelMapper.map(newsArticleDto, NewsArticle.class);
        return modelMapper.map(newsArticleRepository.save(newsArticle), NewsArticleDto.class);
    }

    @Override
    public void deleteById(String id) {
        newsArticleRepository.deleteById(id);
    }
}