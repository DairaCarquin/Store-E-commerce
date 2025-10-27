package com.store.ecommerce.infrastructure.persistence.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64;

    private boolean active = true;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
