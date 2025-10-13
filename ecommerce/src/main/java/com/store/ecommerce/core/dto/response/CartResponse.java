package com.store.ecommerce.core.dto.response;

import java.util.List;

public record CartResponse(List<CartItemResponse> items, Double subtotal, Double discount, Double total) {
}
