package com.store.ecommerce.core.dto.request;

public record RegisterRequest(String fullName, String email, String password, String phone, String dni) {
}
