package br.com.feedhub.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Feed Hub API",
                version = "1.0",
                description = "Feed Hub API",
                termsOfService = "https://github.com/sahid-sousa/feed-hub",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Sahid Sousa",
                        url = "https://github.com/sahid-sousa"
                ),
                license = @io.swagger.v3.oas.annotations.info.License(
                        name = "Apache 2.0",
                        url = "https://github.com/sahid-sousa/feed-hub"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT authentication using a Bearer token"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI(); // The OpenAPI definition is primarily handled by annotations
    }

}
