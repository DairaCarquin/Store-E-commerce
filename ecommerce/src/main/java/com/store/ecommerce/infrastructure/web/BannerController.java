package com.store.ecommerce.infrastructure.web;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.store.ecommerce.core.dto.request.BannerRequest;
import com.store.ecommerce.core.dto.response.BannerResponse;
import com.store.ecommerce.core.service.BannerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public List<BannerResponse> getAll() {
        return bannerService.findAllActive().stream()
                .map(b -> new BannerResponse(
                        b.getId(),
                        b.getTitle(),
                        b.getDescription(),
                        b.getButtonText(),
                        b.getButtonLink(),
                        b.getImageBase64(),
                        b.isActive()))
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BannerResponse create(@RequestBody BannerRequest req) {
        var b = bannerService.create(req);
        return new BannerResponse(b.getId(), b.getTitle(), b.getDescription(),
                b.getButtonText(), b.getButtonLink(), b.getImageBase64(), b.isActive());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannerResponse> uploadBanner(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String buttonText,
            @RequestParam String buttonLink,
            @RequestParam MultipartFile image) throws IOException {
        var saved = bannerService.createWithImage(title, description, buttonText, buttonLink, image);
        var response = new BannerResponse(saved.getId(), saved.getTitle(), saved.getDescription(),
                saved.getButtonText(), saved.getButtonLink(), "[BASE64]", saved.isActive());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
