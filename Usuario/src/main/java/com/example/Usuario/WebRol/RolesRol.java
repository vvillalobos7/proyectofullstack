package com.example.Usuario.WebRol;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Component
public class RolesRol {

    private final WebClient webClient;

    public RolesRol(@Value("${rol-service.url}") String rolServiceUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(rolServiceUrl)
            .build();
    }

    public Map<String, Object> getRolesById(Long id) {
        try {
            return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Error al obtener rol: " + body))))
                .bodyToMono(Map.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error HTTP al consumir servicio de rol: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el servicio de rol", e);
        }
    }
}