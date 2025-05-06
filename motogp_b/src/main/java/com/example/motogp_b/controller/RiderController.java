package com.example.motogp_b.controller;

import com.example.motogp_b.dto.RiderDto;
import com.example.motogp_b.service.RiderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RiderController {
    RiderService riderService;

    @GetMapping
    ResponseEntity<List<RiderDto>> getRiders(RiderDto riderDto) {
        return ResponseEntity.ok(riderService.findAll(riderDto));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<RiderDto> createRider(@RequestPart("rider") RiderDto riderDto,
                                         @RequestPart("photo") MultipartFile photoFile) {
        return ResponseEntity.ok(riderService.create(riderDto, photoFile));
    }

    @GetMapping("/{id}")
    ResponseEntity<RiderDto> getRider(@PathVariable String id) {
        return ResponseEntity.ok(riderService.findById(id));
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<RiderDto> updateRider(@PathVariable String id,
                                         @RequestPart("rider") RiderDto riderDto,
                                         @RequestPart(value = "photo", required = false) MultipartFile photoFile) {
        return ResponseEntity.ok(riderService.update(id, riderDto, photoFile));
    }


    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteRider(@PathVariable String id) {
        riderService.deleteById(id);
        return ResponseEntity.ok("Rider deleted successfully");
    }
}