package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ContractDto;
import com.example.motogp_b.entity.Contract;
import com.example.motogp_b.repository.ContractRepository;
import com.example.motogp_b.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractServiceImpl implements ContractService {
    ContractRepository contractRepository;
    ModelMapper modelMapper;

    @Override
    public List<ContractDto> findAll(ContractDto contractDto) {
        return contractRepository.findAllContract(contractDto).stream()
                .map(contract -> modelMapper.map(contract, ContractDto.class))
                .toList();
    }

    @Override
    public ContractDto findById(String id) {
        return modelMapper.map(contractRepository.findById(id), ContractDto.class);
    }

    @Override
    public ContractDto create(ContractDto contractDto) {
        Contract contract = modelMapper.map(contractDto, Contract.class);
        return modelMapper.map(contractRepository.save(contract), ContractDto.class);
    }

    @Override
    public ContractDto update(String id, ContractDto contractDto) {
        Contract contract = modelMapper.map(contractDto, Contract.class);
        return modelMapper.map(contractRepository.save(contract), ContractDto.class);
    }

    @Override
    public void deleteById(String id) {
        contractRepository.deleteById(id);
    }
}