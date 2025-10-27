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

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepo;

    public Page<Product> list(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    public Product get(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
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

        return productRepository.save(p);
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

        return productRepository.save(p);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    public Page<Product> listByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepo.existsById(categoryId)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        return productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable);
    }

    public Page<Product> search(String name, Double price, Pageable pageable) {
        if (name != null && price != null) {
            return productRepository.findByNameContainingIgnoreCaseOrPrice(name, BigDecimal.valueOf(price), pageable);
        } else if (name != null) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (price != null) {
            return productRepository.findByPrice(BigDecimal.valueOf(price), pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

}
