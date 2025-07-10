package com.example.Usuario.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("Microservicio de Usuarios - AgroTech")
                .version("1.0.0")
                .description("Gestión de usuarios que interactúan con el sistema AgroTech SPA.")
                .contact(new Contact()
                    .name("Equipo AgroTech")
                    .email("soporte@agrotech.cl")
                    .url("https://agrotech.cl"))
                .license(new License()
                    .name("Licencia MIT")
                    .url("https://opensource.org/licenses/MIT"))
            );
    }
}