package com.electricity.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Electricity Usage Tracking API",
                version = "1.0.0",
                description = "Backend API for tracking household electricity usage, visualizing trends, and computing bills under a tiered pricing model.",
                contact = @Contact(
                        name = "Backend Developer",
                        email = "developer@electricity.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Development Server")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
}