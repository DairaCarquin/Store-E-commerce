package com.store.ecommerce.infrastructure.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SwaggerStartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Aplicación iniciada correctamente!");
        System.out.println("Swagger UI disponible en: http://localhost:8845/swagger-ui/index.html");
        System.out.println("Documentación OpenAPI en: http://localhost:8845/v3/api-docs");
        System.out.println("============================================================");
    }
}