package com.example.motogp_b.controller;

import com.example.motogp_b.dto.EventDto;
import com.example.motogp_b.service.EventService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {
    EventService eventService;

    @GetMapping
    ResponseEntity<List<EventDto>> getEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @PostMapping
    ResponseEntity<EventDto> createEvent(@RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.create(eventDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<EventDto> getEvent(@PathVariable String id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<EventDto> updateEvent(@PathVariable String id, @RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.update(id, eventDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteEvent(@PathVariable String id) {
        eventService.deleteById(id);
        return ResponseEntity.ok("Event deleted successfully");
    }
}