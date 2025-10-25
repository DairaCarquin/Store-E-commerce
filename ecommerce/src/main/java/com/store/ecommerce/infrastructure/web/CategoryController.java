package com.store.ecommerce.infrastructure.web;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.store.ecommerce.core.dto.request.CategoryRequest;
import com.store.ecommerce.core.dto.response.CategoryResponse;
import com.store.ecommerce.core.dto.response.CategoryWithProductsResponse;
import com.store.ecommerce.core.dto.response.ProductResponse;
import com.store.ecommerce.core.service.CategoryService;
import com.store.ecommerce.infrastructure.persistence.entity.Category;
import com.store.ecommerce.infrastructure.persistence.entity.Product;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> list() {
        return categoryService.findAll().stream()
                .map(c -> new CategoryResponse(
                        c.getId(),
                        c.getName(),
                        c.getDescription(),
                        c.getImageBase64(),
                        c.isActive()))
                .toList();
    }

    @GetMapping("/{id}")
    public CategoryResponse getOne(@PathVariable Long id) {
        var c = categoryService.findById(id);
        return new CategoryResponse(c.getId(), c.getName(), c.getDescription(), c.getImageBase64(), c.isActive());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse create(@RequestBody CategoryRequest req) {
        Category c = new Category();
        c.setName(req.name());
        c.setDescription(req.description());
        c.setImageBase64(req.imageBase64());
        var saved = categoryService.save(c);
        return new CategoryResponse(saved.getId(), saved.getName(), saved.getDescription(),
                saved.getImageBase64(), saved.isActive());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse uploadCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image) {

        try {
            String base64 = Base64.getEncoder().encodeToString(image.getBytes());
            Category c = new Category();
            c.setName(name);
            c.setDescription(description);
            c.setImageBase64(base64);
            var saved = categoryService.save(c);

            return new CategoryResponse(saved.getId(), saved.getName(),
                    saved.getDescription(), "[BASE64]", saved.isActive());
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(@PathVariable Long id, @RequestBody CategoryRequest req) {
        var updated = categoryService.update(id, req);
        return new CategoryResponse(updated.getId(), updated.getName(),
                updated.getDescription(), updated.getImageBase64(), updated.isActive());
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateImage(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            Category c = categoryService.findById(id);
            c.setImageBase64(Base64.getEncoder().encodeToString(image.getBytes()));
            categoryService.save(c);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }

    @GetMapping("/with-products")
    public List<CategoryWithProductsResponse> listWithProducts() {
        return categoryService.findAll().stream()
                .map(category -> {
                    var products = category.getProducts().stream()
                            .filter(Product::isActive)
                            .map(p -> new ProductResponse(
                                    p.getId(),
                                    p.getName(),
                                    p.getDescription(),
                                    p.getPrice().doubleValue(),
                                    p.getStock(),
                                    p.getImageBase64(),
                                    p.getCategory() != null ? p.getCategory().getId() : null,
                                    p.getCategory() != null ? p.getCategory().getName() : null,
                                    p.isActive()))
                            .toList();

                    return new CategoryWithProductsResponse(
                            category.getId(),
                            category.getName(),
                            category.getDescription(),
                            category.getImageBase64(),
                            category.isActive(),
                            products);
                })
                .toList();
    }

}
