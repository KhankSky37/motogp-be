package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ResultDto;
import com.example.motogp_b.entity.Result;
import com.example.motogp_b.repository.ResultRepository;
import com.example.motogp_b.service.ResultService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResultServiceImpl implements ResultService {
    ResultRepository resultRepository;
    ModelMapper modelMapper;

    @Override
    public List<ResultDto> findAll() {
        return resultRepository.findAll().stream()
                .map(result -> modelMapper.map(result, ResultDto.class))
                .toList();
    }

    @Override
    public ResultDto findById(String id) {
        return modelMapper.map(resultRepository.findById(id), ResultDto.class);
    }

    @Override
    public ResultDto create(ResultDto resultDto) {
        Result result = modelMapper.map(resultDto, Result.class);
        return modelMapper.map(resultRepository.save(result), ResultDto.class);
    }

    @Override
    public ResultDto update(String id, ResultDto resultDto) {
        Result result = modelMapper.map(resultDto, Result.class);
        return modelMapper.map(resultRepository.save(result), ResultDto.class);
    }

    @Override
    public void deleteById(String id) {
        resultRepository.deleteById(id);
    }
}