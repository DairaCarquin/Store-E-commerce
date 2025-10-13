package com.store.ecommerce.core.dto.response;

public record ProductResponse(
        Long id, String name, String description, Double price, Integer stock, String imageUrl, String category,
        Boolean active) {
}