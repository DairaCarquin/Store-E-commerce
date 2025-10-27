package com.store.ecommerce.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String line1;
  private String line2;
  private String city;
  private String state;

  @Column(name = "zip_code")
  private String zipCode;

  private String country = "Peru";

  @Column(name = "is_default")
  private boolean isDefault;
}
