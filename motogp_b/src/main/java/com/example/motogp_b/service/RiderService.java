package com.example.motogp_b.service;

import com.example.motogp_b.dto.RiderDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RiderService {
    List<RiderDto> findAll(RiderDto riderDto);

    RiderDto findById(String id);

    RiderDto create(RiderDto riderDto, MultipartFile file);

    RiderDto update(String id, RiderDto riderDto, MultipartFile photoFile);

    void deleteById(String id);
}