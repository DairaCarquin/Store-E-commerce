package com.store.ecommerce.infrastructure.persistence.entity;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
  name = "cart_items",
  uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"})
)
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_base64", columnDefinition = "TEXT")
    private String imageBase64;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_snapshot", nullable = false)
    private BigDecimal priceSnapshot;
}
