package com.example.motogp_b.service;

import com.example.motogp_b.dto.ResultDto;

import java.util.List;

public interface ResultService {
    List<ResultDto> findAll();

    List<ResultDto> findAll(String sessionId, String riderId, String teamId, String manufacturerId, Integer position,
            String status);
            

    ResultDto findById(String id);

    ResultDto create(ResultDto resultDto);

    ResultDto update(String id, ResultDto resultDto);

    void deleteById(String id);
}