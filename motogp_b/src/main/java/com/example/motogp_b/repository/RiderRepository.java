package com.example.motogp_b.repository;

import com.example.motogp_b.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends JpaRepository<Rider, String> {
    boolean existsByRiderId(String riderId);
}