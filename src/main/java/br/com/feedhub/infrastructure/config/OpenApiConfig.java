package br.com.feedhub.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Feed Hub API")
                        .version("1.0")
                        .description("Feed Hub API")
                        .termsOfService("https://github.com/sahid-sousa/feed-hub")
                        .license(
                                new License()
                                        .name("Apache 2.0")
                                        .url("https://github.com/sahid-sousa/feed-hub")
                        )
                );
    }

}
