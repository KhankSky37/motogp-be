package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.CircuitDto;
import com.example.motogp_b.entity.Circuit;
import com.example.motogp_b.repository.CircuitRepository;
import com.example.motogp_b.service.CircuitService;
import com.example.motogp_b.service.FileStorageService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import com.example.motogp_b.entity.Event;
import com.example.motogp_b.repository.EventRepository;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CircuitServiceImpl implements CircuitService {
    CircuitRepository circuitRepository;
    EventRepository eventRepository;
    ModelMapper modelMapper;
    FileStorageService fileStorageService;

    private static final String CIRCUIT_SUBDIRECTORY = "circuits";

    @Override
    public List<CircuitDto> findAll(CircuitDto circuitDto) {
        return circuitRepository.findAllCircuits(circuitDto).stream()
                .map(circuit -> modelMapper.map(circuit, CircuitDto.class))
                .toList();
    }

    @Override
    public CircuitDto findById(String id) {
        return modelMapper.map(circuitRepository.findById(id), CircuitDto.class);
    }

    @Override
    public CircuitDto create(CircuitDto circuitDto, MultipartFile imageFile) {
        Circuit circuit = modelMapper.map(circuitDto, Circuit.class);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = fileStorageService.storeFile(imageFile, CIRCUIT_SUBDIRECTORY);
                circuit.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Could not process image file for circuit: " + circuitDto.getName(), e);
            }
        }

        Circuit savedCircuit = circuitRepository.save(circuit);
        return modelMapper.map(savedCircuit, CircuitDto.class);
    }

    @Override
    @Transactional
    public CircuitDto update(String id, CircuitDto circuitDto, MultipartFile imageFile) {
        Circuit existingCircuit = circuitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circuit not found with ID: " + id));

        String oldImageUrl = existingCircuit.getImageUrl();
        String newImageUrl = oldImageUrl;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                if (StringUtils.hasText(oldImageUrl)) {
                    try {
                        fileStorageService.deleteFile(oldImageUrl);
                    } catch (IOException e) {
                        System.err.println(
                                "WARN: Could not delete old image file: " + oldImageUrl + " - " + e.getMessage());
                    }
                }
                newImageUrl = fileStorageService.storeFile(imageFile, CIRCUIT_SUBDIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Could not store new image file for circuit: " + id, e);
            }
        }

        modelMapper.map(circuitDto, existingCircuit);
        existingCircuit.setImageUrl(newImageUrl);

        Circuit updatedCircuit = circuitRepository.save(existingCircuit);
        return modelMapper.map(updatedCircuit, CircuitDto.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        circuitRepository.findById(id).ifPresent(circuit -> {
            try {
                if (StringUtils.hasText(circuit.getImageUrl())) {
                    fileStorageService.deleteFile(circuit.getImageUrl());
                    List<Event> events = eventRepository.findByCircuitId(id);
                    for (Event event : events) {
                        event.setCircuit(null);
                        eventRepository.save(event);
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not delete image file: " + circuit.getImageUrl() + " for circuit ID: " + id);
            }
            circuitRepository.deleteById(id);
        });
    }
}