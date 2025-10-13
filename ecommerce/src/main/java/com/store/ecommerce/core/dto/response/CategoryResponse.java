package com.store.ecommerce.core.dto.response;

public record CategoryResponse(Long id, String name, String description, String imageUrl, boolean active) {
}
