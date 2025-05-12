package com.example.motogp_b.controller;

import com.example.motogp_b.dto.NewsArticleDto;
import com.example.motogp_b.service.NewsArticleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsArticleController {
    NewsArticleService newsArticleService;

    @GetMapping
    ResponseEntity<List<NewsArticleDto>> getNewsArticles(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(newsArticleService.findAll(keyword));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<NewsArticleDto> createNewsArticle(@RequestPart("newsArticle") NewsArticleDto newsArticleDto,
                                                     @RequestPart("photo") MultipartFile photoFile) {
        return ResponseEntity.ok(newsArticleService.create(newsArticleDto, photoFile));
    }

    @GetMapping("/{id}")
    ResponseEntity<NewsArticleDto> getNewsArticle(@PathVariable String id) {
        return ResponseEntity.ok(newsArticleService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<NewsArticleDto> updateNewsArticle(@PathVariable String id,
                                                     @RequestPart("newsArticle") NewsArticleDto newsArticleDto,
                                                     @RequestPart(value = "photo", required = false) MultipartFile photoFile) {
        return ResponseEntity.ok(newsArticleService.update(id, newsArticleDto, photoFile));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteNewsArticle(@PathVariable String id) {
        newsArticleService.deleteById(id);
        return ResponseEntity.ok("News article deleted successfully");
    }
}