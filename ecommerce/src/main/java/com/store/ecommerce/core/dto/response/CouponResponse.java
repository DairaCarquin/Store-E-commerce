package com.store.ecommerce.core.dto.response;

public record CouponResponse(String code, boolean active, Double discount, String expiresAt) {
}
