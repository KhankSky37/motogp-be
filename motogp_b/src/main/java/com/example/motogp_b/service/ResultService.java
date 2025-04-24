package com.example.motogp_b.service;

import com.example.motogp_b.dto.ResultDto;

import java.util.List;

public interface ResultService {
    List<ResultDto> findAll();

    ResultDto findById(String id);

    ResultDto create(ResultDto resultDto);

    ResultDto update(String id, ResultDto resultDto);

    void deleteById(String id);
}