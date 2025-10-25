package com.store.ecommerce.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.ecommerce.infrastructure.persistence.entity.Logo;

@Repository
public interface LogoRepository extends JpaRepository<Logo, Long> {
    Optional<Logo> findByActiveTrue();
}
