package com.store.ecommerce.core.dto.response;

import lombok.Data;

@Data
public class LogoResponse {
    private Long id;
    private String name;
    private String imageBase64;
    private String mimeType;
}