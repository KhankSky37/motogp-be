package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.ResultDto;
import com.example.motogp_b.entity.Result;
import com.example.motogp_b.entity.Session;
import com.example.motogp_b.repository.ResultRepository;
import com.example.motogp_b.repository.SessionRepository;
import com.example.motogp_b.service.ResultService;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResultServiceImpl implements ResultService {
    ResultRepository resultRepository;
    SessionRepository sessionRepository;
    ModelMapper modelMapper;

    private static final List<Integer> RACE_POINTS_DISTRIBUTION = Arrays.asList(
            25, 20, 16, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);

    private static final List<Integer> SPRINT_RACE_POINTS_DISTRIBUTION = Arrays.asList(
            12, 9, 7, 6, 5, 4, 3, 2, 1);

    @Override
    public List<ResultDto> findAll() {
        return resultRepository.findAll().stream()
                .map(result -> modelMapper.map(result, ResultDto.class))
                .toList();
    }

    @Override
    public List<ResultDto> findAll(String sessionId, String riderId, String teamId, String manufacturerId,
            Integer position,
            String status) {
        return resultRepository.search(sessionId, riderId, teamId, manufacturerId, position, status).stream()
                .map(resultEntity -> {
                    ResultDto resultDto = modelMapper.map(resultEntity, ResultDto.class);
                    if (resultEntity.getSession() != null) {
                        resultDto.setSessionId(resultEntity.getSession().getId());
                    }
                    return resultDto;
                })
                .toList();
    }

    @Override
    public ResultDto findById(String id) {
        return resultRepository.findById(id)
                .map(result -> modelMapper.map(result, ResultDto.class))
                .orElse(null);
    }

    @Override
    public ResultDto create(ResultDto resultDto) {
        Result result = modelMapper.map(resultDto, Result.class);

        if (resultDto.getSessionId() != null) {
            Session session = sessionRepository.findById(resultDto.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found with id: " + resultDto.getSessionId()));
            result.setSession(session);

            String sessionTypeLower = session.getSessionType().toLowerCase();
            Integer position = result.getPosition();

            if (position != null && position > 0) {
                int points = 0;
                if ("race".equalsIgnoreCase(sessionTypeLower)) {
                    points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
                } else if ("sprint".equalsIgnoreCase(sessionTypeLower)) {
                    points = getPointsFromDistribution(position, SPRINT_RACE_POINTS_DISTRIBUTION);
                } else if (sessionTypeLower.startsWith("q")) {
                    points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
                }
                result.setPoints(points);
            } else {
                result.setPoints(0);
            }
        } else {
            result.setPoints(0);
        }

        Result savedResult = resultRepository.save(result);
        return modelMapper.map(savedResult, ResultDto.class);
    }

    private int getPointsFromDistribution(int position, List<Integer> pointsDistribution) {
        if (position > 0 && position <= pointsDistribution.size()) {
            return pointsDistribution.get(position - 1);
        }
        return 0;
    }

    @Override
    public ResultDto update(String id, ResultDto resultDto) {
        // Find the existing result to verify it exists
        Result existingResult = resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + id));

        // Lưu lại các thông tin audit cũ
        LocalDateTime createdDate = existingResult.getCreatedDate();
        String createdBy = existingResult.getCreateUser();

        // Map from DTO but preserve the ID and session if not explicitly changed
        Result result = modelMapper.map(resultDto, Result.class);
        result.setId(id);

        // Giữ lại thông tin audit
        result.setCreatedDate(createdDate);
        result.setCreateUser(createdBy);

        // Calculate points based on position and session type, just like in create
        // method
        if (resultDto.getSessionId() != null) {
            Session session = sessionRepository.findById(resultDto.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found with id: " + resultDto.getSessionId()));
            result.setSession(session);

            String sessionTypeLower = session.getSessionType().toLowerCase();
            Integer position = result.getPosition();

            if (position != null && position > 0) {
                int points = 0;
                if ("race".equalsIgnoreCase(sessionTypeLower)) {
                    points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
                } else if ("sprint".equalsIgnoreCase(sessionTypeLower)) {
                    points = getPointsFromDistribution(position, SPRINT_RACE_POINTS_DISTRIBUTION);
                } else if (sessionTypeLower.startsWith("q")) {
                    points = getPointsFromDistribution(position, RACE_POINTS_DISTRIBUTION);
                }
                result.setPoints(points);
            } else {
                result.setPoints(0);
            }
        } else {
            // If sessionId is not provided in the DTO, keep the existing session
            result.setSession(existingResult.getSession());
            result.setPoints(existingResult.getPoints()); // Keep existing points
        }

        Result savedResult = resultRepository.save(result);
        ResultDto updatedDto = modelMapper.map(savedResult, ResultDto.class);

        // Explicitly set sessionId in the returning DTO
        if (savedResult.getSession() != null) {
            updatedDto.setSessionId(savedResult.getSession().getId());
        }

        return updatedDto;
    }

    @Override
    public void deleteById(String id) {
        resultRepository.deleteById(id);
    }
}