package com.foodey.server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Foodey Service API",
            version = "1.0",
            description = "Documentation Foodey Service API v1.0"))
public class OpenApiConfig {}
