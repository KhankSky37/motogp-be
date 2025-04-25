package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {
    boolean existsById(Integer id);
}