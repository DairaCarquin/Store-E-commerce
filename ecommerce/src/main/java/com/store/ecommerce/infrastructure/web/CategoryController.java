package com.store.ecommerce.infrastructure.web;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.CategoryRequest;
import com.store.ecommerce.core.dto.response.CategoryResponse;
import com.store.ecommerce.core.service.CategoryService;
import com.store.ecommerce.infrastructure.persistence.entity.Category;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.findAll().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getDescription(), c.getImageUrl(),
                        c.isActive()))
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse create(@RequestBody CategoryRequest req) {
        var c = new Category();
        c.setName(req.name());
        c.setDescription(req.description());
        c.setImageUrl(req.imageUrl());
        return new CategoryResponse(categoryService.save(c).getId(), req.name(), req.description(), req.imageUrl(),
                true);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(@PathVariable Long id, @RequestBody CategoryRequest req) {
        var updated = categoryService.update(id, req);
        return new CategoryResponse(updated.getId(), updated.getName(), updated.getDescription(), updated.getImageUrl(),
                updated.isActive());
    }
}
