package com.store.ecommerce.core.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.ecommerce.core.dto.request.CategoryRequest;
import com.store.ecommerce.infrastructure.persistence.entity.Category;
import com.store.ecommerce.infrastructure.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public List<Category> findAll() {
        return categoryRepo.findAll();
    }

    public Category findById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public Category save(Category c) {
        if (categoryRepo.existsByNameIgnoreCase(c.getName()))
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        return categoryRepo.save(c);
    }

    public Category update(Long id, CategoryRequest req) {
        Category c = findById(id);
        c.setName(req.name());
        c.setDescription(req.description());
        c.setImageBase64(req.imageBase64());
        return categoryRepo.save(c);
    }

    public void delete(Long id) {
        if (!categoryRepo.existsById(id))
            throw new RuntimeException("Categoría no encontrada");
        categoryRepo.deleteById(id);
    }
}
