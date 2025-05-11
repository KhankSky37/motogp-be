package com.example.motogp_b.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "news_article")
public class NewsArticle extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "article_link")
    private String articleLink;

    @Column(name = "article_type")
    private String articleType;
}