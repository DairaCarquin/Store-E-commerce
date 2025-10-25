package com.store.ecommerce.core.dto.request;

public record BannerRequest(
        String title,
        String description,
        String buttonText,
        String buttonLink,
        String imageBase64,
        Boolean active) {
}
