package com.store.ecommerce.infrastructure.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.ProductRequest;
import com.store.ecommerce.core.dto.response.ProductResponse;
import com.store.ecommerce.core.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductResponse> all(Pageable pageable) {
        return productService.list(pageable)
                .map(p -> new ProductResponse(
                        p.getId(), p.getName(), p.getDescription(),
                        p.getPrice().doubleValue(), p.getStock(),
                        p.getImageUrl(),
                        p.getCategory() != null ? p.getCategory().getName() : null,
                        p.isActive()));
    }

    @GetMapping("/{id}")
    public ProductResponse one(@PathVariable Long id) {
        var p = productService.get(id);
        return new ProductResponse(
                p.getId(), p.getName(), p.getDescription(),
                p.getPrice().doubleValue(), p.getStock(),
                p.getImageUrl(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.isActive());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse create(@RequestBody ProductRequest req) {
        var p = productService.create(req);
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(), p.getPrice().doubleValue(), p.getStock(),
                p.getImageUrl(), p.getCategory().getName(), p.isActive());
    }
}
