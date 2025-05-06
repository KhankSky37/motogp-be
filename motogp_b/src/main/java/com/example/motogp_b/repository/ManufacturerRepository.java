package com.example.motogp_b.repository;

import com.example.motogp_b.dto.ManufacturerDto;
import com.example.motogp_b.entity.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, String> {
    @Query("""
            SELECT m FROM Manufacturer m
            WHERE
                ( :#{#manufacturerDto.keyword} IS NULL OR :#{#manufacturerDto.keyword} = '' OR
                  m.name LIKE %:#{#manufacturerDto.keyword}% )
                AND
                ( :#{#manufacturerDto.locationCountry} IS NULL OR :#{#manufacturerDto.locationCountry} = '' OR
                  m.locationCountry = :#{#manufacturerDto.locationCountry} )
            """)
    List<Manufacturer> findAllManufacturers(@Param("manufacturerDto") ManufacturerDto manufacturerDto);
}