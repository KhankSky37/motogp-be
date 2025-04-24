package com.example.motogp_b.service;

import com.example.motogp_b.dto.RiderDto;

import java.util.List;

public interface RiderService {
    List<RiderDto> findAll();

    RiderDto findById(String id);

    RiderDto create(RiderDto riderDto);

    RiderDto update(String id, RiderDto riderDto);

    void deleteById(String id);
}