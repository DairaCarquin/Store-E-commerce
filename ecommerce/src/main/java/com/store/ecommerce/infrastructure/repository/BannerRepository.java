package com.store.ecommerce.infrastructure.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.store.ecommerce.infrastructure.persistence.entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByActiveTrueOrderByIdAsc();
}
