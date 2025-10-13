package com.store.ecommerce.core.dto.response;

public record CartItemResponse(Long productId, String name, int quantity, Double price) {
}
