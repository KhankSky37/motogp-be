package com.example.motogp_b.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * DTO for {@link com.example.motogp_b.entity.NewsArticle}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsArticleDto{
    LocalDateTime createdDate;
    String createUser;
    Date modifiedDate;
    String modifiedUser;
    String id;
    String title;
    Instant publishDate;
    String imageUrl;
    String articleLink;
    String articleType;
}