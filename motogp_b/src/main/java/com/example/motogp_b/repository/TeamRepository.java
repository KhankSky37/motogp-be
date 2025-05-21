package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    List<Team> findByManufacturerId(String manufacturerId);
}