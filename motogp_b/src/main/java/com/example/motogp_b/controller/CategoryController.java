package com.example.motogp_b.controller;

import com.example.motogp_b.dto.CategoryDto;
import com.example.motogp_b.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    ResponseEntity<List<CategoryDto>> getCategories() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @PostMapping
    ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.create(categoryDto));
    }

    @GetMapping("/{id}")
    ResponseEntity<CategoryDto> getCategory(@PathVariable String id) {

        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<CategoryDto> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
          
        return ResponseEntity.ok(categoryService.update(id, categoryDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable String id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}