package com.store.ecommerce.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "banners")
@Data
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Column(name = "button_text")
    private String buttonText;

    @Column(name = "button_link")
    private String buttonLink;

    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64;

    private boolean active = true;
}
