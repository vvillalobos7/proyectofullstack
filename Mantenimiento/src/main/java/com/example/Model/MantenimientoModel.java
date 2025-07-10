package com.example.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un mantenimiento programado o realizado para una maquinaria.")
public class MantenimientoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del mantenimiento", example = "1")
    private Long id;

    @Column(name = "fecha_programada", nullable = false)
    @Schema(description = "Fecha programada del mantenimiento", example = "2025-08-10T10:00:00")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_realizada")
    @Schema(description = "Fecha en la que se realizó el mantenimiento", example = "2025-08-12T14:30:00")
    private LocalDateTime fechaRealizada;

    @Column(nullable = false)
    @Schema(description = "Tipo de mantenimiento (por ejemplo, 'Preventivo' o 'Correctivo')", example = "Preventivo")
    private String tipo;

    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del registro", example = "2025-07-09T12:00:00")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    @Schema(description = "Fecha de última actualización del registro", example = "2025-07-09T12:00:00")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.fechaCreacion = now;
        this.fechaActualizacion = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}