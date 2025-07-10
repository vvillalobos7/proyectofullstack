package com.example.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API de Pedidos - AgroTech")
                .version("1.0.0")
                .description("Microservicio para la gestión de pedidos realizados por usuarios en AgroTech. " +
                             "Permite crear, consultar, actualizar, eliminar y realizar seguimiento de pedidos.")
                .contact(new Contact()
                    .name("Equipo AgroTech")
                    .email("soporte@agrotech.cl")
                    .url("https://www.agrotech.cl"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}