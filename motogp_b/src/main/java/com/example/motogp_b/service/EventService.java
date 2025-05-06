package com.example.motogp_b.service;

import com.example.motogp_b.dto.EventDto;

import java.util.List;

public interface EventService {
    List<EventDto> findAll();

    List<EventDto> findAllWithFilters(String keyword, String seasonId, String circuitId,
            String eventType, String startDateFrom, String startDateTo);

    EventDto findById(String id);

    EventDto create(EventDto eventDto);

    EventDto update(String id, EventDto eventDto);

    void deleteById(String id);
}