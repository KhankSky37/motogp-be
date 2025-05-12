package com.example.motogp_b.controller;

import com.example.motogp_b.dto.ContractDto;
import com.example.motogp_b.service.ContractService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contracts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractController {
    ContractService contractService;

    @GetMapping
    ResponseEntity<List<ContractDto>> getContracts(ContractDto contractDto) {
        return ResponseEntity.ok(contractService.findAll(contractDto));
    }

    @PostMapping
    ResponseEntity<ContractDto> createContract(@RequestBody ContractDto contractDto) {
        return ResponseEntity.ok(contractService.create(contractDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<ContractDto> getContract(@PathVariable String id) {
        return ResponseEntity.ok(contractService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<ContractDto> updateContract(@PathVariable String id, @RequestBody ContractDto contractDto) {
        return ResponseEntity.ok(contractService.update(id, contractDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteContract(@PathVariable String id) {
        contractService.deleteById(id);
        return ResponseEntity.ok("Contract deleted successfully");
    }
}