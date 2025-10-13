package com.store.ecommerce.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.store.ecommerce.infrastructure.persistence.enums.OrderStatus;
import com.store.ecommerce.infrastructure.persistence.enums.PaymentMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity @Table(name="orders")
@Data
public class Order {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional=false) @JoinColumn(name="user_id")
  private User user;

  private LocalDateTime createdAt = LocalDateTime.now();
  @Enumerated(EnumType.STRING) private OrderStatus status = OrderStatus.PENDIENTE;

  @ManyToOne @JoinColumn(name="shipping_address_id")
  private Address shippingAddress;

  @Column(nullable=false) private BigDecimal subtotal;
  @Column(nullable=false) private BigDecimal discount = BigDecimal.ZERO;
  @Column(nullable=false) private BigDecimal total;

  @Enumerated(EnumType.STRING) private PaymentMethod paymentMethod = PaymentMethod.TARJETA;
  private String trackingCode;
  private String couponCode;

  @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<OrderItem> items = new ArrayList<>();
}
