package com.example.motogp_b.service;

import com.example.motogp_b.dto.CircuitDto;

import java.util.List;

public interface CircuitService {
    List<CircuitDto> findAll();

    CircuitDto findById(String id);

    CircuitDto create(CircuitDto circuitDto);

    CircuitDto update(String id, CircuitDto circuitDto);

    void deleteById(String id);
}