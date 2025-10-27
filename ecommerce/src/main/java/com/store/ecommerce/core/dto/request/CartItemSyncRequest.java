package com.store.ecommerce.core.dto.request;

import lombok.Data;

@Data
public class CartItemSyncRequest {
    private Long productId;
    private int quantity;
}
