package com.example.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("Mantenimiento de maquinarias - AgroTech")
                .version("1.0.0")
                .description("Microservicio para gestionar mantenimientos de maquinaria arrendada y comprada en AgroTech.")
                .contact(new Contact()
                    .name("Soporte AgroTech")
                    .email("soporte@agrotech.cl")
                )
            );
    }
}