package com.example.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("AgroTech - Entrega de Pedidos API")
                .version("1.0.0")
                .description("API para gestionar la entrega de pedidos de los usuarios de AgroTech.")
                .termsOfService("https://www.agrotech.com/terms")
                .contact(new Contact()
                    .name("Soporte AgroTech")
                    .email("soporte@agrotech.com")
                    .url("https://www.agrotech.com"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT"))
            );
    }
}