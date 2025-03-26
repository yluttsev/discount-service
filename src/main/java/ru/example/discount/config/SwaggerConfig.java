package ru.example.discount.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "API Discount system",
                description = "API системы скидок",
                version = "1.0.0"
        )
)
public class SwaggerConfig {
}
