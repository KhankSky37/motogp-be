package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    @Query("SELECT s FROM Session s WHERE " +
            "(:eventId IS NULL OR s.event.id = :eventId) AND " +
            "(:categoryId IS NULL OR s.category.id = :categoryId) AND " +
            "(:sessionType IS NULL OR s.sessionType = :sessionType) AND " +
            "(:fromDate IS NULL OR s.sessionDatetime >= :fromDate) AND " +
            "(:toDate IS NULL OR s.sessionDatetime <= :toDate)")
    List<Session> search(
            @Param("eventId") String eventId,
            @Param("categoryId") String categoryId,
            @Param("sessionType") String sessionType,
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate);
}