package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer> {
    boolean existsById(Integer id);
    
    @Query("SELECT s FROM Season s WHERE LOWER(s.name) LIKE :keyword OR CAST(s.id AS string) LIKE :keyword")
    List<Season> search(@Param("keyword") String keyword);
}