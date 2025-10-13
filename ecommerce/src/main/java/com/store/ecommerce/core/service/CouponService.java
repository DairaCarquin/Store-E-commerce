package com.store.ecommerce.core.service;

import org.springframework.stereotype.Service;

import com.store.ecommerce.infrastructure.persistence.entity.Coupon;
import com.store.ecommerce.infrastructure.repository.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepo;

    public Coupon create(Coupon c) {
        return couponRepo.save(c);
    }

    public Coupon validate(String code) {
        Coupon c = couponRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Cupón no encontrado"));
        if (!c.isValidNow())
            throw new RuntimeException("Cupón expirado o inactivo");
        return c;
    }

    public void delete(Long id) {
        couponRepo.deleteById(id);
    }
}