package com.store.ecommerce.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
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

    @Column(name = "percent_off")
    private BigDecimal percentOff;

    @Column(name = "amount_off")
    private BigDecimal amountOff;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    private boolean active = true;

    public boolean isValidNow() {
        return active && (expiresAt == null || LocalDateTime.now().isBefore(expiresAt));
    }
}
