package com.example.motogp_b.service;

import com.example.motogp_b.dto.ManufacturerDto;

import java.util.List;

public interface ManufacturerService {
    List<ManufacturerDto> findAll();
    ManufacturerDto findById(String id);
    ManufacturerDto create(ManufacturerDto manufacturerDto);
    ManufacturerDto update(String id, ManufacturerDto manufacturerDto);
    void deleteById(String id);
}