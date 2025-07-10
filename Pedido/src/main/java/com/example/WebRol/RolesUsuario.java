package com.example.WebRol;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Component
public class RolesUsuario {
  private WebClient webClient = null;

    public void UsuarioCliente(@Value("${usuario-service.url}") String usuarioServiceUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(usuarioServiceUrl)
            .build();
    }

    public Map<String, Object> getUsuarioById(Long id) {
        try {
            return this.webClient.get()
                .uri("/api/v1/usuario/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(String.class)
                        .flatMap(body -> Mono.error(new RuntimeException("Error al obtener usuario: " + body)))
                )
                .bodyToMono(Map.class)
                .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error HTTP al consumir servicio de usuario: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el servicio de usuario", e);
        }
    }
}
