package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.EventDto;
import com.example.motogp_b.entity.Event;
import com.example.motogp_b.repository.EventRepository;
import com.example.motogp_b.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventServiceImpl implements EventService {
    EventRepository eventRepository;
    ModelMapper modelMapper;

    @Override
    public List<EventDto> findAll() {
        return eventRepository.findAll().stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public List<EventDto> findAllWithFilters(String keyword, String seasonId, String circuitId,
            String eventType, String startDateFrom, String startDateTo) {
        // Convert string dates to LocalDate if they're not null
        LocalDate fromDate = startDateFrom != null && !startDateFrom.isEmpty() ? LocalDate.parse(startDateFrom) : null;
        LocalDate toDate = startDateTo != null && !startDateTo.isEmpty() ? LocalDate.parse(startDateTo) : null;

        // Convert seasonId to Integer if it's not null
        Integer seasonIdInt = seasonId != null && !seasonId.isEmpty() ? Integer.parseInt(seasonId) : null;

        List<Event> events = eventRepository.findAllWithFilters(
                keyword, seasonIdInt, circuitId, eventType, fromDate, toDate);

        return events.stream()
                .map(event -> modelMapper.map(event, EventDto.class))
                .toList();
    }

    @Override
    public EventDto findById(String id) {
        return modelMapper.map(eventRepository.findById(id), EventDto.class);
    }

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        return modelMapper.map(eventRepository.save(event), EventDto.class);
    }

    @Override
    public EventDto update(String id, EventDto eventDto) {
        Event event = modelMapper.map(eventDto, Event.class);
        return modelMapper.map(eventRepository.save(event), EventDto.class);
    }

    @Override
    public void deleteById(String id) {
        eventRepository.deleteById(id);
    }
}