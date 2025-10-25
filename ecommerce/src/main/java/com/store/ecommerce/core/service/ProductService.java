package com.store.ecommerce.core.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.store.ecommerce.core.dto.request.ProductRequest;
import com.store.ecommerce.infrastructure.persistence.entity.Category;
import com.store.ecommerce.infrastructure.persistence.entity.Product;
import com.store.ecommerce.infrastructure.repository.CategoryRepository;
import com.store.ecommerce.infrastructure.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public Page<Product> list(Pageable pageable) {
        return productRepo.findByActiveTrue(pageable);
    }

    public Product get(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Product save(Product product) {
        return productRepo.save(product);
    }

    public Product create(ProductRequest req) {
        Product p = new Product();
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(BigDecimal.valueOf(req.price()));
        p.setStock(req.stock());
        p.setImageBase64(req.imageBase64());
        p.setActive(req.active() != null ? req.active() : true);

        if (req.categoryId() != null) {
            Category category = categoryRepo.findById(req.categoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no válida"));
            p.setCategory(category);
        }

        return productRepo.save(p);
    }

    public Product update(Long id, ProductRequest req) {
        Product p = get(id);
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(BigDecimal.valueOf(req.price()));
        p.setStock(req.stock());
        p.setImageBase64(req.imageBase64());
        p.setActive(req.active() != null ? req.active() : true);

        if (req.categoryId() != null) {
            Category c = categoryRepo.findById(req.categoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría inválida"));
            p.setCategory(c);
        }

        return productRepo.save(p);
    }

    public void delete(Long id) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepo.deleteById(id);
    }

    public Page<Product> listByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        return productRepo.findByCategoryIdAndActiveTrue(categoryId, pageable);
    }

}
