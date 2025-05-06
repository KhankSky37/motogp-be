package com.example.motogp_b.service;

import com.example.motogp_b.dto.SeasonDto;

import java.util.List;

public interface SeasonService {
    List<SeasonDto> findAll(String keyword);
    SeasonDto findById(Integer id);
    SeasonDto create(SeasonDto seasonDto);
    SeasonDto update(Integer id, SeasonDto seasonDto);
    void deleteById(Integer id);
}
