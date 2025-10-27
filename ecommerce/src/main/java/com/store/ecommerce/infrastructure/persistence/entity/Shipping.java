package com.store.ecommerce.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.store.ecommerce.infrastructure.persistence.enums.ShippingStatus;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shipping")
@Data
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;

    private String provider;

    @Enumerated(EnumType.STRING)
    private ShippingStatus status = ShippingStatus.EN_PREPARACION;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate = LocalDateTime.now();
}
