package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ManufacturerDto;
import com.example.motogp_b.entity.Manufacturer;
import com.example.motogp_b.repository.ManufacturerRepository;
import com.example.motogp_b.service.ManufacturerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufacturerServiceImpl implements ManufacturerService {
    ManufacturerRepository manufacturerRepository;
    ModelMapper modelMapper;

    @Override
    public List<ManufacturerDto> findAll() {
        return manufacturerRepository.findAll().stream()
                .map(manufacturer -> modelMapper.map(manufacturer, ManufacturerDto.class))
                .toList();
    }

    @Override
    public ManufacturerDto findById(String id) {
        return modelMapper.map(manufacturerRepository.findById(id), ManufacturerDto.class);
    }

    @Override
    public ManufacturerDto create(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = modelMapper.map(manufacturerDto, Manufacturer.class);
        return modelMapper.map(manufacturerRepository.save(manufacturer), ManufacturerDto.class);
    }

    @Override
    public ManufacturerDto update(String id, ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = modelMapper.map(manufacturerDto, Manufacturer.class);
        return modelMapper.map(manufacturerRepository.save(manufacturer), ManufacturerDto.class);
    }

    @Override
    public void deleteById(String id) {
        manufacturerRepository.deleteById(id);
    }
}