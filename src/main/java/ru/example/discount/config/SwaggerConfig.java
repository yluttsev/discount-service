package ru.example.discount.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
        @Bean
        public OpenAPI discountsOpenAPI() {
                return new OpenAPI().info(new Info()
                                .title("Discounts API")
                                .version("1.0.0")
                                .description("Система вычисления скидок")
                        );
        }
}
