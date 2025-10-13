package com.store.ecommerce.core.service;

import org.springframework.stereotype.Service;

import com.store.ecommerce.infrastructure.persistence.entity.User;
import com.store.ecommerce.infrastructure.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;

    public User getUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User updateUser(Long id, User updated) {
        User user = getUser(id);
        user.setFullName(updated.getFullName());
        user.setPhone(updated.getPhone());
        user.setDni(updated.getDni());
        return userRepo.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepo.existsById(id))
            throw new RuntimeException("Usuario no encontrado");
        userRepo.deleteById(id);
    }
}