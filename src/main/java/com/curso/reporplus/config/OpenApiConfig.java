package com.curso.reporplus.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI reporPlusOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .info(new Info()
                        .title("Repor+ API")
                        .description(
                                "API para controle de estoque e reposição "
                                        + "de peças de caminhões, oficinas "
                                        + "e máquinas agrícolas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Repor+ 2026")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do projeto Repor+")
                        .url("https://github.com/walldeicortezc/reporplus2026"));
    }
}
