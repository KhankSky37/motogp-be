package com.example.motogp_b.repository;

import com.example.motogp_b.dto.RiderDto;
import com.example.motogp_b.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, String> {
    Boolean existsByRiderId(String riderId);

    @Query("""
            SELECT r FROM Rider r
            left join Contract c on r.riderId = c.riderId
            WHERE
                ( :#{#riderDto.keyword} IS NULL OR :#{#riderDto.keyword} = '' OR
                  r.riderId LIKE %:#{#riderDto.keyword}% OR
                  r.firstName LIKE %:#{#riderDto.keyword}% OR
                  r.lastName LIKE %:#{#riderDto.keyword}% )
            AND
                ( :#{#riderDto.nationality} IS NULL OR :#{#riderDto.nationality} = '' OR
                  r.nationality = :#{#riderDto.nationality} )
            AND (:#{#riderDto.teamId} IS NULL OR :#{#riderDto.teamId} = '' OR
                  c.teamId = :#{#riderDto.teamId} )
            """)
    List<Rider> findAllRider(@Param("riderDto")RiderDto riderDto);
}