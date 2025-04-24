package com.example.motogp_b.service;

import com.example.motogp_b.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    CategoryDto findById(String id);
    CategoryDto create(CategoryDto categoryDto);
    CategoryDto update(String id, CategoryDto categoryDto);
    void deleteById(String id);
}