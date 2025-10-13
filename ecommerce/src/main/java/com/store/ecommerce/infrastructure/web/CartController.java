package com.store.ecommerce.infrastructure.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.AddItemRequest;
import com.store.ecommerce.core.dto.request.UpdateQtyRequest;
import com.store.ecommerce.core.dto.response.CartItemResponse;
import com.store.ecommerce.core.dto.response.CartResponse;
import com.store.ecommerce.core.service.CartService;
import com.store.ecommerce.infrastructure.persistence.entity.Cart;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable Long userId) {
        var c = cartService.getOrCreate(userId);
        return mapToResponse(c);
    }

    @PostMapping("/{userId}/items")
    public CartResponse add(@PathVariable Long userId, @RequestBody AddItemRequest req) {
        return mapToResponse(cartService.addItem(userId, req.productId(), req.quantity()));
    }

    @PutMapping("/{userId}/items/{productId}")
    public CartResponse update(@PathVariable Long userId, @PathVariable Long productId,
            @RequestBody UpdateQtyRequest req) {
        return mapToResponse(cartService.updateItem(userId, productId, req.quantity()));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public CartResponse remove(@PathVariable Long userId, @PathVariable Long productId) {
        return mapToResponse(cartService.updateItem(userId, productId, 0));
    }

    @DeleteMapping("/{userId}")
    public CartResponse clear(@PathVariable Long userId) {
        return mapToResponse(cartService.clear(userId));
    }

    private CartResponse mapToResponse(Cart cart) {
        var items = cart.getItems().stream()
                .map(ci -> new CartItemResponse(ci.getProduct().getId(), ci.getProduct().getName(),
                        ci.getQuantity(), ci.getPriceSnapshot().doubleValue()))
                .toList();
        return new CartResponse(items, cart.getSubtotal().doubleValue(), cart.getDiscount().doubleValue(),
                cart.getTotal().doubleValue());
    }
}
