package com.store.ecommerce.infrastructure.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.CreateOrderRequest;
import com.store.ecommerce.core.dto.response.OrderItemResponse;
import com.store.ecommerce.core.dto.response.OrderResponse;
import com.store.ecommerce.core.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(@RequestBody CreateOrderRequest req, @RequestParam Long userId) {
        var order = orderService.createFromCart(userId, req.shippingAddressId(), req.paymentMethod());
        var items = order.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProductName(), i.getQuantity(), i.getUnitPrice().doubleValue()))
                .toList();
        return new OrderResponse(order.getId(), order.getStatus().name(),
                order.getSubtotal().doubleValue(), order.getTotal().doubleValue(),
                items, order.getPaymentMethod().name(), order.getTrackingCode());
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> byUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId).stream()
                .map(o -> new OrderResponse(o.getId(), o.getStatus().name(), o.getSubtotal().doubleValue(),
                        o.getTotal().doubleValue(), List.of(), o.getPaymentMethod().name(), o.getTrackingCode()))
                .toList();
    }
}
