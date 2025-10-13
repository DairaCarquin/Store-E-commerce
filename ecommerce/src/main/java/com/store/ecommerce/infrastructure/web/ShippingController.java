package com.store.ecommerce.infrastructure.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.UpdateShippingRequest;
import com.store.ecommerce.core.dto.response.ShippingResponse;
import com.store.ecommerce.core.service.ShippingService;
import com.store.ecommerce.infrastructure.persistence.enums.ShippingStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shipping")
@RequiredArgsConstructor
public class ShippingController {
    private final ShippingService shippingService;

    @GetMapping("/{orderId}")
    public ShippingResponse get(@PathVariable Long orderId) {
        var s = shippingService.getByOrder(orderId);
        return new ShippingResponse(s.getOrder().getId(), s.getStatus().name(), s.getProvider());
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ShippingResponse update(@PathVariable Long orderId, @RequestBody UpdateShippingRequest req) {
        var s = shippingService.update(orderId,
                ShippingStatus.valueOf(req.status()), req.provider());
        return new ShippingResponse(s.getOrder().getId(), s.getStatus().name(), s.getProvider());
    }
}
