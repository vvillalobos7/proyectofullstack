package com.example.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "entrega")
public class EntregaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la entrega", example = "1")
    private Long idEntrega;

    @Column(nullable = false)
    @Schema(description = "Dirección de la entrega", example = "Calle Falsa 123")
    private String direccionEntrega;

    @Column(nullable = false)
    @Schema(description = "Ciudad de destino", example = "Santiago")
    private String ciudad;

    @Column(nullable = false)
    @Schema(description = "Región de destino", example = "Metropolitana")
    private String region;

    @Column
    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;

    @Column
    @Schema(description = "Persona de contacto", example = "Juan Pérez")
    private String personaContacto;

    @Column
    @Schema(description = "Fecha programada para la entrega", example = "2025-07-09T11:00:00")
    private LocalDateTime fechaProgramada;

    @Column
    @Schema(description = "Fecha en que se realizó la entrega", example = "2025-07-09T12:00:00")
    private LocalDateTime fechaRealizada;

    @Column(nullable = false)
    @Schema(description = "Estado de la entrega", example = "PROGRAMADA")
    private String estado;

    @Column
    @Schema(description = "Transportista encargado de la entrega", example = "Juan Pérez")
    private String transportista;

    @Column
    @Schema(description = "Vehículo utilizado para la entrega", example = "Camioneta 4x4")
    private String vehiculo;

    @Column
    @Schema(description = "Patente del vehículo", example = "AB123CD")
    private String patente;

    @Column(nullable = false)
    @Schema(description = "Tipo de entrega", example = "URGENTE")
    private String tipoEntrega;

    @Column
    @Schema(description = "Observaciones adicionales de la entrega", example = "Entregar en mano")
    private String observaciones;

    @Column
    @Schema(description = "Motivo de cancelación, si aplica", example = "Cliente canceló la entrega")
    private String motivoCancelacion;

    @Column
    @Schema(description = "Fecha de creación del registro", example = "2025-07-09T10:00:00")
    private LocalDateTime fechaCreacion;

    @Column
    @Schema(description = "Fecha de la última actualización", example = "2025-07-09T10:30:00")
    private LocalDateTime fechaActualizacion;

    @Column
    @Schema(description = "Costo estimado de la entrega", example = "1000.0")
    private Double costo = 0.0;

    @Column
    @Schema(description = "Kilómetros recorridos durante la entrega", example = "20")
    private Integer kmRecorridos = 0;

    @PrePersist
    private void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null || this.estado.trim().isEmpty()) {
            this.estado = "PROGRAMADA";
        }
    }

    @PreUpdate
    private void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}