package com.store.ecommerce.core.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.ecommerce.core.dto.request.LoginRequest;
import com.store.ecommerce.core.dto.request.RegisterRequest;
import com.store.ecommerce.core.dto.response.AuthResponse;
import com.store.ecommerce.infrastructure.persistence.entity.User;
import com.store.ecommerce.infrastructure.persistence.enums.Role;
import com.store.ecommerce.infrastructure.repository.UserRepository;
import com.store.ecommerce.infrastructure.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest req) {
        User user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getDni(),
                user.getRole(),
                user.getId());
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setFullName(req.fullName());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setPhone(req.phone());
        user.setDni(req.dni());
        user.setRole(Role.USER);
        userRepo.save(user);

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(
                token,
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getDni(),
                user.getRole(),
                user.getId());
    }

}