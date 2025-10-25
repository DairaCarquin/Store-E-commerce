package com.store.ecommerce.core.service;

import com.store.ecommerce.infrastructure.persistence.entity.Logo;
import com.store.ecommerce.infrastructure.repository.LogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogoService {

    private final LogoRepository logoRepo;

    public List<Logo> findAll() {
        return logoRepo.findAll();
    }

    public Logo findById(Long id) {
        return logoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Logo no encontrado"));
    }

    public Logo save(Logo logo) {
        return logoRepo.save(logo);
    }

    public void delete(Long id) {
        if (!logoRepo.existsById(id)) {
            throw new RuntimeException("Logo no encontrado");
        }
        logoRepo.deleteById(id);
    }
}
