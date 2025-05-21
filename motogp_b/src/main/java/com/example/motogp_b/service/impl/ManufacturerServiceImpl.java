package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ManufacturerDto;
import com.example.motogp_b.entity.Manufacturer;
import com.example.motogp_b.entity.Result;
import com.example.motogp_b.entity.Team;
import com.example.motogp_b.repository.ManufacturerRepository;
import com.example.motogp_b.repository.ResultRepository;
import com.example.motogp_b.repository.TeamRepository;
import com.example.motogp_b.service.ManufacturerService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ManufacturerServiceImpl implements ManufacturerService {
    ManufacturerRepository manufacturerRepository;
    TeamRepository teamRepository;
    ResultRepository resultRepository;
    ModelMapper modelMapper;

    @Override
    public List<ManufacturerDto> findAll() {
        return manufacturerRepository.findAll().stream()
                .map(manufacturer -> modelMapper.map(manufacturer, ManufacturerDto.class))
                .toList();
    }

    @Override
    public List<ManufacturerDto> findAll(ManufacturerDto manufacturerDto) {
        return manufacturerRepository.findAllManufacturers(manufacturerDto).stream()
                .map(manufacturer -> modelMapper.map(manufacturer, ManufacturerDto.class))
                .toList();
    }

    @Override
    public ManufacturerDto findById(String id) {
        Optional<Manufacturer> manufacturerOptional = manufacturerRepository.findById(id);
        if (manufacturerOptional.isPresent()) {
            return modelMapper.map(manufacturerOptional.get(), ManufacturerDto.class);
        }
        throw new RuntimeException("Không tìm thấy nhà sản xuất với ID: " + id);
    }

    @Override
    public ManufacturerDto create(ManufacturerDto manufacturerDto) {
        Manufacturer manufacturer = modelMapper.map(manufacturerDto, Manufacturer.class);
        Manufacturer savedManufacturer = manufacturerRepository.save(manufacturer);
        return modelMapper.map(savedManufacturer, ManufacturerDto.class);
    }

    @Override
    public ManufacturerDto update(String id, ManufacturerDto manufacturerDto) {
        // Kiểm tra xem manufacturer có tồn tại không
        Manufacturer existingManufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà sản xuất với ID: " + id));

        // Chỉ cập nhật các trường cần thiết, giữ nguyên thông tin audit
        existingManufacturer.setName(manufacturerDto.getName());
        existingManufacturer.setLocationCountry(manufacturerDto.getLocationCountry());

        // Lưu entity đã cập nhật và trả về DTO
        Manufacturer updatedManufacturer = manufacturerRepository.save(existingManufacturer);
        return modelMapper.map(updatedManufacturer, ManufacturerDto.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        try {
            // Get the manufacturer to be deleted
            Manufacturer manufacturer = manufacturerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà sản xuất với ID: " + id));

            // Set null for all Teams that reference this manufacturer
            List<Team> teamsWithManufacturer = teamRepository.findByManufacturerId(id);
            for (Team team : teamsWithManufacturer) {
                team.setManufacturer(null);
                teamRepository.save(team);
            }

            // Set null for all Results that reference this manufacturer
            List<Result> resultsWithManufacturer = resultRepository.findByManufacturerId(id);
            for (Result result : resultsWithManufacturer) {
                result.setManufacturer(null);
                resultRepository.save(result);
            }

            // Now safely delete the manufacturer
            manufacturerRepository.deleteById(id);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete manufacturer with ID: " + id +
                    ". Error: " + ex.getMessage(), ex);
        }
    }
}