package com.store.ecommerce.core.dto.response;

public record UserResponse(Long id, String fullName, String email, String phone, String dni, String role) {
}
