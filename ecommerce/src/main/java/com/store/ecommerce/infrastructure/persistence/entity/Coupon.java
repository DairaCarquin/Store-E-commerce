package com.store.ecommerce.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    private BigDecimal percentOff;
    private BigDecimal amountOff;
    private LocalDateTime expiresAt;
    private boolean active = true;

    public boolean isValidNow() {
        return active && (expiresAt == null || LocalDateTime.now().isBefore(expiresAt));
    }
}