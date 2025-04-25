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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RiderServiceImpl implements RiderService {
    RiderRepository riderRepository;
    ModelMapper modelMapper;

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
    public RiderDto create(RiderDto riderDto) {
        // Kiểm tra xem ID đã tồn tại chưa
        if (riderRepository.existsByRiderId(riderDto.getRiderId())) {
            throw new RuntimeException("ID người lái đã tồn tại. Vui lòng sử dụng ID khác.");
        }

        Rider rider = modelMapper.map(riderDto, Rider.class);
        return modelMapper.map(riderRepository.save(rider), RiderDto.class);
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
        riderRepository.deleteById(id);
    }
}