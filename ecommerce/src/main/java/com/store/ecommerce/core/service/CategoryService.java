package com.store.ecommerce.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.store.ecommerce.core.dto.request.CategoryRequest;
import com.store.ecommerce.infrastructure.persistence.entity.Category;
import com.store.ecommerce.infrastructure.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category save(Category c) {
        if (categoryRepo.existsByNameIgnoreCase(c.getName()))
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        return categoryRepo.save(c);
    }

    public Category update(Long id, CategoryRequest req) {
        Category c = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        c.setName(req.name());
        c.setDescription(req.description());
        c.setImageUrl(req.imageUrl());
        return categoryRepo.save(c);
    }
}