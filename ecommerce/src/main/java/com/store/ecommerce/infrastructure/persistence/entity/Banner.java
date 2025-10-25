package com.store.ecommerce.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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

    @Lob
    @Column(name = "image_base64", columnDefinition = "LONGTEXT")
    private String imageBase64;

    private boolean active = true;
}
