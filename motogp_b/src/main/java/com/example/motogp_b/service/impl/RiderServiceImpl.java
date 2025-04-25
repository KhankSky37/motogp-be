package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.RiderDto;
import com.example.motogp_b.entity.Rider;
import com.example.motogp_b.repository.RiderRepository;
import com.example.motogp_b.service.FileStorageService;
import com.example.motogp_b.service.RiderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RiderServiceImpl implements RiderService {
    RiderRepository riderRepository;
    ModelMapper modelMapper;
    FileStorageService fileStorageService;

    private static final String RIDER_SUBDIRECTORY = "riders";

    @Override
    public List<RiderDto> findAll() {
        return riderRepository.findAll().stream()
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
        if(riderRepository.existsByRiderId(rider.getRiderId())){
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
    public RiderDto update(String id, RiderDto riderDto) {
        // Kiểm tra nếu ID mới khác với ID cũ và đã tồn tại
        if (!id.equals(riderDto.getRiderId()) && riderRepository.existsByRiderId(riderDto.getRiderId())) {
            throw new RuntimeException("ID người lái đã tồn tại. Vui lòng sử dụng ID khác.");
        }

        Rider rider = modelMapper.map(riderDto, Rider.class);
        return modelMapper.map(riderRepository.save(rider), RiderDto.class);
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