package com.example.motogp_b.repository;

import com.example.motogp_b.entity.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {
    Boolean existsNewsArticleById(String id);

    @Query("""
           SELECT n
           FROM NewsArticle n
           WHERE (:keyword IS NULL OR LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(n.articleType) LIKE LOWER(CONCAT('%', :keyword, '%')))
           """)
    List<NewsArticle> findAllNewsArticles(String keyword);

}