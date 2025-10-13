package com.store.ecommerce.core.dto.request;

import com.store.ecommerce.infrastructure.persistence.enums.PaymentMethod;

public record CreateOrderRequest(Long shippingAddressId, PaymentMethod paymentMethod) {
}
