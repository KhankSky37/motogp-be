package com.example.motogp_b.repository;

import com.example.motogp_b.dto.CircuitDto;
import com.example.motogp_b.entity.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CircuitRepository extends JpaRepository<Circuit, String> {
    @Query("""
            SELECT c FROM Circuit c
            WHERE
                ( :#{#circuitDto.keyword} IS NULL OR :#{#circuitDto.keyword} = '' OR
                  c.name LIKE %:#{#circuitDto.keyword}% OR
                  c.locationCity LIKE %:#{#circuitDto.keyword}% OR
                  c.locationCountry LIKE %:#{#circuitDto.keyword}% )
                AND
                ( :#{#circuitDto.locationCountry} IS NULL OR :#{#circuitDto.locationCountry} = '' OR
                  c.locationCountry = :#{#circuitDto.locationCountry} )
            """)
    List<Circuit> findAllCircuits(@Param("circuitDto") CircuitDto circuitDto);
}