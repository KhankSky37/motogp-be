package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Circuit;
import com.example.motogp_b.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
         List<Event> findByCircuitId(String circuitId);
        List<Event> findBySeasonId(Integer seasonId);

    @Query("SELECT e FROM Event e " +
            "WHERE (:keyword IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "       OR LOWER(e.officialName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:seasonId IS NULL OR e.season.id = :seasonId) " +
            "AND (:circuitId IS NULL OR e.circuit.id = :circuitId) " +
            "AND (:eventType IS NULL OR e.eventType = :eventType) " +
            "AND (:startDateFrom IS NULL OR e.startDate >= :startDateFrom) " +
            "AND (:startDateTo IS NULL OR e.startDate <= :startDateTo)")
    List<Event> findAllWithFilters(
            @Param("keyword") String keyword,
            @Param("seasonId") Integer seasonId,
            @Param("circuitId") String circuitId,
            @Param("eventType") String eventType,
            @Param("startDateFrom") LocalDate startDateFrom,
            @Param("startDateTo") LocalDate startDateTo);
}