package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.CircuitDto;
import com.example.motogp_b.entity.Circuit;
import com.example.motogp_b.repository.CircuitRepository;
import com.example.motogp_b.service.CircuitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CircuitServiceImpl implements CircuitService {
    CircuitRepository circuitRepository;
    ModelMapper modelMapper;
    
    @Override
    public List<CircuitDto> findAll() {
        return circuitRepository.findAll().stream()
                .map(circuit -> modelMapper.map(circuit, CircuitDto.class))
                .toList();
    }

    @Override
    public CircuitDto findById(String id) {
        return modelMapper.map(circuitRepository.findById(id), CircuitDto.class);
    }

    @Override
    public CircuitDto create(CircuitDto circuitDto) {
        Circuit circuit = modelMapper.map(circuitDto, Circuit.class);
        return modelMapper.map(circuitRepository.save(circuit), CircuitDto.class);
    }

    @Override
    public CircuitDto update(String id, CircuitDto circuitDto) {
        Circuit circuit = modelMapper.map(circuitDto, Circuit.class);
        return modelMapper.map(circuitRepository.save(circuit), CircuitDto.class);
    }

    @Override
    public void deleteById(String id) {
        circuitRepository.deleteById(id);
    }
}