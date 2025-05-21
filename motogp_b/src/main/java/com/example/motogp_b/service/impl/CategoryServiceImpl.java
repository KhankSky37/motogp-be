package com.example.motogp_b.service.impl;

import com.example.motogp_b.dto.CategoryDto;
import com.example.motogp_b.entity.Category;
import com.example.motogp_b.entity.Session;

import com.example.motogp_b.repository.CategoryRepository;
import com.example.motogp_b.repository.SessionRepository;
import com.example.motogp_b.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    SessionRepository sessionRepository;
    ModelMapper modelMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
    }

    @Override
    public CategoryDto findById(String id) {
        return modelMapper.map(categoryRepository.findById(id), CategoryDto.class);
    }

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        // Kiểm tra xem ID đã tồn tại chưa
        if (categoryRepository.existsByCategoryId(categoryDto.getCategoryId())) {
            throw new RuntimeException("ID danh mục đã tồn tại. Vui lòng sử dụng ID khác.");
        }

        Category category = modelMapper.map(categoryDto, Category.class);
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto update(String id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));
        category.setName(categoryDto.getName());

        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        List<Session> sessions = sessionRepository.findByCategoryCategoryId(id);
        for (Session session : sessions) {
            session.setCategory(null);
            sessionRepository.save(session);
        }
        categoryRepository.deleteById(id);
    }
}