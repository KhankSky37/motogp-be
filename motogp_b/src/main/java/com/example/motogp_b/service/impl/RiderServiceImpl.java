package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.RiderDto;
import com.example.motogp_b.entity.Rider;
import com.example.motogp_b.repository.RiderRepository;
import com.example.motogp_b.service.FileStorageService;
import com.example.motogp_b.service.RiderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RiderServiceImpl implements RiderService {
    RiderRepository riderRepository;
    ModelMapper modelMapper;
    FileStorageService fileStorageService;

    private static final String RIDER_SUBDIRECTORY = "riders";

    @Override
    public List<RiderDto> findAll(RiderDto riderDto) {
        return riderRepository.findAllRider(riderDto).stream()
                .map(rider -> modelMapper.map(rider, RiderDto.class))
                .toList();
    }

    @Override
    public RiderDto findById(String id) {
        return modelMapper.map(riderRepository.findById(id), RiderDto.class);
    }

    @Override
    public RiderDto create(RiderDto riderDto, MultipartFile photoFile) {
        Rider rider = modelMapper.map(riderDto, Rider.class);
        if (riderRepository.existsByRiderId(rider.getRiderId())) {
            throw new RuntimeException("Rider with this ID already exists");
        }

        try {
            String photoUrl = fileStorageService.storeFile(photoFile, RIDER_SUBDIRECTORY);
            rider.setPhotoUrl(photoUrl);
        } catch (IOException e) {
            throw new RuntimeException("Could not process photo file for rider: " + riderDto.getRiderId(), e);
        }

        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }


    @Override
    @Transactional
    public RiderDto update(String id, RiderDto riderDto, MultipartFile photoFile) {
        Rider existingRider = riderRepository.findById(id).orElseThrow(() -> new RuntimeException("Rider not found with ID: " + id));

        String newRiderId = riderDto.getRiderId();
        if (!Objects.equals(id, newRiderId) && riderRepository.existsByRiderId(newRiderId)) {
            throw new RuntimeException("New Rider ID '" + newRiderId + "' already exists. Please use a different ID.");
        }

        String oldPhotoUrl = existingRider.getPhotoUrl();
        String newPhotoUrl = oldPhotoUrl;

        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                if (StringUtils.hasText(oldPhotoUrl)) {
                    try {
                        fileStorageService.deleteFile(oldPhotoUrl);
                    } catch (IOException e) {
                        System.err.println("WARN: Could not delete old photo file: " + oldPhotoUrl + " - " + e.getMessage());
                    }
                }
                newPhotoUrl = fileStorageService.storeFile(photoFile, RIDER_SUBDIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("Could not store new photo file for rider: " + newRiderId, e);
            }
        }
        modelMapper.map(riderDto, existingRider);
        existingRider.setPhotoUrl(newPhotoUrl);

        Rider updatedRider = riderRepository.save(existingRider);
        return modelMapper.map(updatedRider, RiderDto.class);
    }


    @Override
    public void deleteById(String id) {
        riderRepository.findById(id).ifPresent(rider -> {
            try {
                fileStorageService.deleteFile(rider.getPhotoUrl());
            } catch (IOException e) {
                System.err.println("Could not delete photo file: " + rider.getPhotoUrl() + " for rider ID: " + id);
            }
            riderRepository.deleteById(id);
        });
    }
}