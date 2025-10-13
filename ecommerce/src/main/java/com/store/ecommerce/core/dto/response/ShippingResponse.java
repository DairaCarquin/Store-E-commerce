package com.store.ecommerce.core.dto.response;

public record ShippingResponse(Long orderId, String status, String provider) {
}
