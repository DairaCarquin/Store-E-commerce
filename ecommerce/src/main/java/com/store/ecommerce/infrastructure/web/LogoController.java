package com.store.ecommerce.infrastructure.web;

import com.store.ecommerce.core.service.LogoService;
import com.store.ecommerce.infrastructure.persistence.entity.Logo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/logos")
@RequiredArgsConstructor
public class LogoController {

    private final LogoService logoService;

    @GetMapping
    public List<Logo> list() {
        return logoService.findAll();
    }

    @GetMapping("/{id}")
    public Logo get(@PathVariable Long id) {
        return logoService.findById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Logo> create(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image) {

        try {
            String base64 = Base64.getEncoder().encodeToString(image.getBytes());
            Logo logo = new Logo();
            logo.setName(name);
            logo.setImage(base64);
            logo.setActive(true);

            return ResponseEntity.ok(logoService.save(logo));
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Logo> update(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Logo existing = logoService.findById(id);
            existing.setName(name);

            if (image != null && !image.isEmpty()) {
                existing.setImage(Base64.getEncoder().encodeToString(image.getBytes()));
            }

            return ResponseEntity.ok(logoService.save(existing));
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
