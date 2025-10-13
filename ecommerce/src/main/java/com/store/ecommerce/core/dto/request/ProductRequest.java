package com.store.ecommerce.core.dto.request;

public record ProductRequest(
        String name, String description, Double price, Integer stock, Long categoryId, String imageUrl,
        Boolean active) {
}
