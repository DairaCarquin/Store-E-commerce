package com.store.ecommerce.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.ecommerce.core.dto.request.UpdateUserRequest;
import com.store.ecommerce.core.dto.response.UserResponse;
import com.store.ecommerce.core.service.UserService;
import com.store.ecommerce.infrastructure.persistence.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        var user = userService.getUser(id);
        return ResponseEntity.ok(new UserResponse(
                user.getId(), user.getFullName(), user.getEmail(), user.getPhone(), user.getDni(),
                user.getRole().name()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        var u = new User();
        u.setFullName(req.fullName());
        u.setPhone(req.phone());
        u.setDni(req.dni());
        var updated = userService.updateUser(id, u);
        return ResponseEntity.ok(new UserResponse(
                updated.getId(), updated.getFullName(), updated.getEmail(), updated.getPhone(), updated.getDni(),
                updated.getRole().name()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
