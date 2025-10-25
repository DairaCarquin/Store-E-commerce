package com.store.ecommerce.core.dto.response;

public record BannerResponse(
        Long id,
        String title,
        String description,
        String buttonText,
        String buttonLink,
        String imageBase64,
        boolean active) {
}
