package com.example.motogp_b.repository;

import com.example.motogp_b.dto.ContractDto;
import com.example.motogp_b.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, String> {
    @Query("""
           SELECT c FROM Contract c
           WHERE (:#{#contractDto.teamId} IS NULL OR :#{#contractDto.teamId} = '' OR c.teamId = :#{#contractDto.teamId})
           AND (:#{#contractDto.riderId} IS NULL OR :#{#contractDto.riderId} = '' OR c.riderId = :#{#contractDto.riderId})
           AND (:#{#contractDto.seasonId} IS NULL OR c.seasonId = :#{#contractDto.seasonId})
           AND (:#{#contractDto.categoryId} IS NULL OR :#{#contractDto.categoryId} = '' OR c.categoryId = :#{#contractDto.categoryId})
           """)
    List<Contract> findAllContract(@Param("contractDto") ContractDto contractDto);
}