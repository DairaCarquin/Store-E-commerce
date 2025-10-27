package com.store.ecommerce.infrastructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;
    
    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
