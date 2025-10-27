package com.store.ecommerce.core.dto.response;

public record ProductResponse(
        Long id, 
        String name, 
        String description, 
        Double price, 
        Integer stock, 
        String imageBase64,  
        Long categoryId,  
        String category,
        Boolean active
        ) {
}