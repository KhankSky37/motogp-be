package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.RiderDto;
import com.example.motogp_b.entity.Rider;
import com.example.motogp_b.repository.RiderRepository;
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

    private static final String UPLOAD_DIR = "static/uploads/riders";
    private static final Path UPLOAD_PATH = Paths.get(UPLOAD_DIR);

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

        // Handle file upload
        if (photoFile != null && !photoFile.isEmpty()) {
            String originalFilename = StringUtils.cleanPath(photoFile.getOriginalFilename());
            String fileExtension = "";
            int lastDotIndex = originalFilename.lastIndexOf('.');
            if (lastDotIndex > 0) {
                fileExtension = originalFilename.substring(lastDotIndex);
            }
            // Generate a unique filename
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            Path filePath = UPLOAD_PATH.resolve(uniqueFilename);

            try {
                // Create directories if they don't exist
                if (!Files.exists(UPLOAD_PATH)) {
                    Files.createDirectories(UPLOAD_PATH);
                }

                // Save the file
                try (InputStream inputStream = photoFile.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                // Set the photo URL (relative path for web access)
                // Assuming '/uploads/riders/' will be the web-accessible path
                rider.setPhotoUrl("/uploads/riders/" + uniqueFilename);

            } catch (IOException e) {
                // Handle exception appropriately (e.g., log error, throw custom exception)
                throw new RuntimeException("Could not save photo file: " + originalFilename, e);
            }
        } else {
            // Optionally set a default photo URL or leave it null
            rider.setPhotoUrl(null); // Or a path to a default image
        }


        Rider savedRider = riderRepository.save(rider);
        return modelMapper.map(savedRider, RiderDto.class);
    }

    @Override
    public RiderDto update(String id, RiderDto riderDto) {
        Rider rider = modelMapper.map(riderDto, Rider.class);
        return modelMapper.map(riderRepository.save(rider), RiderDto.class);
    }

    @Override
    public void deleteById(String id) {
        riderRepository.deleteById(id);
    }
}