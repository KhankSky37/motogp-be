package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.SessionDto;
import com.example.motogp_b.entity.Session;
import com.example.motogp_b.repository.SessionRepository;
import com.example.motogp_b.service.SessionService;
import lombok.AccessLevel;

import com.example.motogp_b.entity.Result;
import com.example.motogp_b.repository.ResultRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SessionServiceImpl implements SessionService {
    SessionRepository sessionRepository;
    ResultRepository resultRepository;
 
    ModelMapper modelMapper;

    @Override
    public List<SessionDto> findAll(String eventId, String categoryId, String sessionType, String dateFrom,
            String dateTo) {
        List<Session> sessions;

        if (eventId == null && categoryId == null && sessionType == null && dateFrom == null && dateTo == null) {
            // No filters, return all
            sessions = sessionRepository.findAll();
        } else {
            // Convert string dates to Instant if provided
            Instant fromDate = null;
            Instant toDate = null;

            try {
                // Parse format from frontend (YYYY-MM-DD HH:mm)
                if (dateFrom != null && !dateFrom.isEmpty()) {
                    fromDate = java.time.LocalDateTime.parse(
                            dateFrom.replace(" ", "T"),
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                            .atZone(java.time.ZoneId.systemDefault()).toInstant();
                }

                if (dateTo != null && !dateTo.isEmpty()) {
                    toDate = java.time.LocalDateTime.parse(
                            dateTo.replace(" ", "T"),
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
                            .atZone(java.time.ZoneId.systemDefault()).toInstant();
                }
            } catch (Exception e) {
                // Log error and continue with null dates
                System.err.println("Error parsing date: " + e.getMessage());
            }

            sessions = sessionRepository.search(eventId, categoryId, sessionType, fromDate, toDate);
        }

        return sessions.stream()
                .map(session -> modelMapper.map(session, SessionDto.class))
                .toList();
    }

    @Override
    public SessionDto findById(String id) {
        return modelMapper.map(sessionRepository.findById(id), SessionDto.class);
    }

    @Override
    public SessionDto create(SessionDto sessionDto) {
        Session session = modelMapper.map(sessionDto, Session.class);
        return modelMapper.map(sessionRepository.save(session), SessionDto.class);
    }

    @Override
    public SessionDto update(String id, SessionDto sessionDto) {
        Session session = modelMapper.map(sessionDto, Session.class);
        return modelMapper.map(sessionRepository.save(session), SessionDto.class);
    }

   @Override
@Transactional
public void deleteById(String id) {
    try {
        // Lấy session cần xóa
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));

        // Gỡ liên kết các result có liên kết đến session này
        List<Result> resultsWithSession = resultRepository.findBySessionId(id);
        for (Result result : resultsWithSession) {
            result.setSession(null); // Gỡ quan hệ session
            resultRepository.save(result);
        }

        // (Tùy chọn) Gỡ các result có liên kết đến manufacturer cùng id
        List<Result> resultsWithManufacturer = resultRepository.findByManufacturerId(id);
        for (Result result : resultsWithManufacturer) {
            result.setManufacturer(null);
            resultRepository.save(result);
        }

        // Cuối cùng xóa session
        sessionRepository.delete(session);
    } catch (Exception e) {
        throw new RuntimeException("Error deleting session: " + e.getMessage(), e);
    }
}

    
}