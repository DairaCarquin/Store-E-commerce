package com.store.ecommerce.core.dto.response;

import java.util.List;

public record CategoryWithProductsResponse(
        Long id,
        String name,
        String description,
        String imageBase64,
        boolean active,
        List<ProductResponse> products
) {}
