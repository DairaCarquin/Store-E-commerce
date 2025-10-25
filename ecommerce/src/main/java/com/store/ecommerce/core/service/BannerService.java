package com.store.ecommerce.core.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.store.ecommerce.core.dto.request.BannerRequest;
import com.store.ecommerce.infrastructure.persistence.entity.Banner;
import com.store.ecommerce.infrastructure.repository.BannerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerService {

    private final BannerRepository bannerRepo;

    public List<Banner> findAllActive() {
        return bannerRepo.findByActiveTrueOrderByIdAsc();
    }

    public Banner get(Long id) {
        return bannerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner no encontrado"));
    }

    public Banner save(Banner b) {
        return bannerRepo.save(b);
    }

    public Banner create(BannerRequest req) {
        Banner b = new Banner();
        b.setTitle(req.title());
        b.setDescription(req.description());
        b.setButtonText(req.buttonText());
        b.setButtonLink(req.buttonLink());
        b.setImageBase64(req.imageBase64());
        b.setActive(req.active() != null ? req.active() : true);
        return bannerRepo.save(b);
    }

    public Banner createWithImage(String title, String description, String buttonText,
            String buttonLink, MultipartFile image) throws IOException {
        Banner b = new Banner();
        b.setTitle(title);
        b.setDescription(description);
        b.setButtonText(buttonText);
        b.setButtonLink(buttonLink);
        b.setImageBase64(Base64.getEncoder().encodeToString(image.getBytes()));
        b.setActive(true);
        return bannerRepo.save(b);
    }

    public void delete(Long id) {
        bannerRepo.deleteById(id);
    }
}
