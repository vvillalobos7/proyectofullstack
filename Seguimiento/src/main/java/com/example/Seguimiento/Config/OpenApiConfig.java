package com.example.Seguimiento.Config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Seguimiento - Agrotech")
                .version("1.0")
                .description("Esta API proporciona funcionalidades para gestionar el seguimiento de las entregas y tareas en Agrotech, permitiendo la creación, actualización y eliminación de registros de seguimiento de manera sencilla y eficiente.")
                .termsOfService("https://www.agrotech.com/terms")
                .contact(new Contact()
                    .name("Soporte de Agrotech")
                    .email("soporte@agrotech.com")
                    .url("https://www.agrotech.com/contacto"))
            )
            .servers(List.of(
                new Server().url("https://api.agrotech.com/v1").description("Servidor de Producción"),
                new Server().url("https://dev.api.agrotech.com/v1").description("Servidor de Desarrollo")
            ));
    }
}

