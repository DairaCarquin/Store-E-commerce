package com.store.ecommerce.core.dto.response;

import java.util.List;

public record OrderResponse(Long id, String status, Double subtotal, Double total,
        List<OrderItemResponse> items, String paymentMethod, String trackingCode) {
}