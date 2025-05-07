package com.example.motogp_b.service;

import com.example.motogp_b.dto.CircuitDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CircuitService {
    List<CircuitDto> findAll(CircuitDto circuitDto);

    CircuitDto findById(String id);

    CircuitDto create(CircuitDto circuitDto, MultipartFile imageFile);

    CircuitDto update(String id, CircuitDto circuitDto, MultipartFile imageFile);

    void deleteById(String id);
}