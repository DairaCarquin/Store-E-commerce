package com.store.ecommerce.infrastructure.repository;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.ecommerce.infrastructure.persistence.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryIdAndActiveTrue(Long categoryId, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByPrice(BigDecimal price, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseOrPrice(String name, BigDecimal price, Pageable pageable);
}