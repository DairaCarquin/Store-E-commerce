package com.store.ecommerce.core.dto.request;

public record AddItemRequest(
    Long productId,
    Integer quantity,
    String name,
    Double price,
    String description,
    String imageBase64
) {}
