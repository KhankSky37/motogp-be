package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    @Query("SELECT r FROM Result r WHERE " +
            "(:sessionId IS NULL OR r.session.id = :sessionId) AND " +
            "(:riderId IS NULL OR r.rider.riderId = :riderId) AND " +
            "(:teamId IS NULL OR r.team.id = :teamId) AND " +
            "(:manufacturerId IS NULL OR r.manufacturer.id = :manufacturerId) AND " +
            "(:position IS NULL OR r.position = :position) AND " +
            "(:status IS NULL OR r.status = :status)")
    List<Result> search(
            @Param("sessionId") String sessionId,
            @Param("riderId") String riderId,
            @Param("teamId") String teamId,
            @Param("manufacturerId") String manufacturerId,
            @Param("position") Integer position,
            @Param("status") String status);
}