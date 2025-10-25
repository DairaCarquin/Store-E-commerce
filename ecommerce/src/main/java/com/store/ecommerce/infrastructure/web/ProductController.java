package com.store.ecommerce.infrastructure.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.store.ecommerce.core.dto.request.ProductRequest;
import com.store.ecommerce.core.dto.response.PageResponse;
import com.store.ecommerce.core.dto.response.ProductResponse;
import com.store.ecommerce.core.service.CategoryService;
import com.store.ecommerce.core.service.ProductService;
import com.store.ecommerce.infrastructure.persistence.entity.Category;
import com.store.ecommerce.infrastructure.persistence.entity.Product;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public Page<ProductResponse> all(Pageable pageable) {
        return productService.list(pageable)
                .map(p -> new ProductResponse(
                        p.getId(), p.getName(), p.getDescription(),
                        p.getPrice().doubleValue(), p.getStock(),
                        p.getImageBase64(),
                        p.getCategory() != null ? p.getCategory().getId() : null,
                        p.getCategory() != null ? p.getCategory().getName() : null,
                        p.isActive()));
    }

    @GetMapping("/{id}")
    public ProductResponse one(@PathVariable Long id) {
        var p = productService.get(id);
        return new ProductResponse(
                p.getId(), p.getName(), p.getDescription(),
                p.getPrice().doubleValue(), p.getStock(),
                p.getImageBase64(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.isActive());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse create(@RequestBody ProductRequest req) {
        var p = productService.create(req);
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(),
                p.getPrice().doubleValue(), p.getStock(), p.getImageBase64(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null, p.isActive());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> uploadProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("stock") Integer stock,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("image") MultipartFile image) {

        try {
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());

            Category category = categoryService.findById(categoryId);
            if (category == null) {
                throw new RuntimeException("Categoría no válida");
            }

            Product p = new Product();
            p.setName(name);
            p.setDescription(description);
            p.setPrice(price);
            p.setStock(stock);
            p.setImageBase64(base64Image);
            p.setActive(true);
            p.setCategory(category);

            Product saved = productService.save(p);

            ProductResponse res = new ProductResponse(
                    saved.getId(), saved.getName(), saved.getDescription(),
                    saved.getPrice().doubleValue(), saved.getStock(),
                    "[BASE64]",
                    saved.getCategory() != null ? saved.getCategory().getId() : null,
                    saved.getCategory() != null ? saved.getCategory().getName() : null,
                    saved.isActive());

            return ResponseEntity.ok(res);

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse update(@PathVariable Long id, @RequestBody ProductRequest req) {
        var updated = productService.update(id, req);
        return new ProductResponse(updated.getId(), updated.getName(), updated.getDescription(),
                updated.getPrice().doubleValue(), updated.getStock(),
                updated.getImageBase64(),
                updated.getCategory() != null ? updated.getCategory().getId() : null,
                updated.getCategory() != null ? updated.getCategory().getName() : null,
                updated.isActive());
    }

    @PutMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {

        try {
            Product p = productService.get(id);
            p.setImageBase64(Base64.getEncoder().encodeToString(image.getBytes()));
            productService.save(p);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public PageResponse<ProductResponse> listByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        return PageResponse.from(
                productService.listByCategory(categoryId, pageable)
                        .map(p -> new ProductResponse(
                                p.getId(),
                                p.getName(),
                                p.getDescription(),
                                p.getPrice().doubleValue(),
                                p.getStock(),
                                p.getImageBase64(),
                                p.getCategory() != null ? p.getCategory().getId() : null, 
                                p.getCategory() != null ? p.getCategory().getName() : null,
                                p.isActive())));
    }

}
