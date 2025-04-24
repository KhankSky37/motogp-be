package com.example.motogp_b.service;

import com.example.motogp_b.dto.ContractDto;

import java.util.List;

public interface ContractService {
    List<ContractDto> findAll();

    ContractDto findById(String id);

    ContractDto create(ContractDto contractDto);

    ContractDto update(String id, ContractDto contractDto);

    void deleteById(String id);
}