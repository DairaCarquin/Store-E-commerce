package com.store.ecommerce.core.dto.request;

public record CouponRequest(String code, Double percentOff, Double amountOff, String expiresAt) {
}
