package com.store.ecommerce.infrastructure.web;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.CouponRequest;
import com.store.ecommerce.core.dto.response.CouponResponse;
import com.store.ecommerce.core.service.CouponService;
import com.store.ecommerce.infrastructure.persistence.entity.Coupon;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CouponResponse create(@RequestBody CouponRequest req) {
        var c = new Coupon();
        c.setCode(req.code());
        if (req.percentOff() != null)
            c.setPercentOff(BigDecimal.valueOf(req.percentOff()));
        if (req.amountOff() != null)
            c.setAmountOff(BigDecimal.valueOf(req.amountOff()));
        c.setActive(true);
        var saved = couponService.create(c);
        return new CouponResponse(saved.getCode(), saved.isActive(),
                saved.getPercentOff() != null ? saved.getPercentOff().doubleValue() : 0,
                saved.getExpiresAt() != null ? saved.getExpiresAt().toString() : null);
    }

    @GetMapping("/{code}")
    public CouponResponse validate(@PathVariable String code) {
        var c = couponService.validate(code);
        return new CouponResponse(c.getCode(), c.isActive(),
                c.getPercentOff() != null ? c.getPercentOff().doubleValue() : 0,
                c.getExpiresAt() != null ? c.getExpiresAt().toString() : null);
    }
}
