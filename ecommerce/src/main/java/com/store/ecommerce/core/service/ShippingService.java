package com.store.ecommerce.core.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.store.ecommerce.infrastructure.persistence.entity.Shipping;
import com.store.ecommerce.infrastructure.persistence.enums.ShippingStatus;
import com.store.ecommerce.infrastructure.repository.ShippingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingService {
    private final ShippingRepository shippingRepo;

    public Shipping getByOrder(Long orderId) {
        return shippingRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
    }

    public Shipping update(Long orderId, ShippingStatus status, String provider) {
        Shipping s = shippingRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        s.setStatus(status);
        if (provider != null)
            s.setProvider(provider);
        s.setLastUpdate(LocalDateTime.now());
        return shippingRepo.save(s);
    }
}