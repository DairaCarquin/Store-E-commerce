package com.store.ecommerce.core.dto.response;

import com.store.ecommerce.infrastructure.persistence.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String fullName;
    private String email;
    private String phone;
    private String dni;
    private Role role;
    private Long userId;
}
