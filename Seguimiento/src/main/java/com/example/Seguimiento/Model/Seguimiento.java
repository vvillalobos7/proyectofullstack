package com.example.Seguimiento.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "seguimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un seguimiento del estado de una entrega.")
public class Seguimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del seguimiento", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Descripción del seguimiento", example = "Pedido entregado al cliente.")
    private String descripcion;

    @Column(nullable = false)
    @Schema(description = "Estado actual del seguimiento", example = "ENTREGADO")
    private String estado;

    @Column(nullable = false)
    @Schema(description = "Fecha de registro del seguimiento", example = "2025-07-09T12:00:00")
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
}