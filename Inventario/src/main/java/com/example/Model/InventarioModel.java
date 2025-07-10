package com.example.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "inventario")
@Schema(description = "Modelo que representa el inventario de un equipo")
public class InventarioModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de inventario", example = "1")
    private Long idInventario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_equipo", nullable = false)
    @NotNull(message = "El equipo es obligatorio")
    @Schema(description = "Equipo asociado al inventario", required = true)
    private EquipoModel equipo;
    
    @Column(nullable = false)
    @NotNull(message = "El stock disponible es obligatorio")
    @Min(value = 0, message = "El stock disponible no puede ser negativo")
    @Schema(description = "Cantidad disponible en stock", example = "10", required = true)
    private Integer stockDisponible;
    
    @Column(nullable = false)
    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Schema(description = "Stock mínimo antes de considerar crítico", example = "5", required = true)
    private Integer stockMinimo;
    
    @Column(nullable = false)
    @NotNull(message = "El stock total es obligatorio")
    @Min(value = 0, message = "El stock total no puede ser negativo")
    @Schema(description = "Stock total registrado", example = "15", required = true)
    private Integer stockTotal;
    
    @Column(nullable = false)
    @NotNull(message = "El stock arrendado es obligatorio")
    @Min(value = 0, message = "El stock arrendado no puede ser negativo")
    @Schema(description = "Cantidad actualmente arrendada", example = "5", required = true)
    private Integer stockArrendado;
    
    @Column(nullable = false)
    @NotBlank(message = "La ubicación es obligatoria")
    @Schema(description = "Ubicación física del inventario", example = "Bodega A", required = true)
    private String ubicacion;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha de última actualización", example = "2025-07-06T10:30:00")
    private Date fechaUltimaActualizacion;
    
    @Column(nullable = false)
    @Schema(description = "Estado del inventario", example = "DISPONIBLE", allowableValues = {"DISPONIBLE", "CRITICO", "AGOTADO"})
    private String estado;
    
    @Schema(description = "Observaciones adicionales", example = "Verificar estado antes de arrendar")
    private String observaciones;
    
    @PrePersist
    @PreUpdate
    private void actualizarInfo() {
        this.fechaUltimaActualizacion = new Date();
        actualizarEstado();
    }
    
    private void actualizarEstado() {
        if (stockDisponible <= 0) {
            this.estado = "AGOTADO";
        } else if (stockDisponible <= stockMinimo) {
            this.estado = "CRITICO";
        } else {
            this.estado = "DISPONIBLE";
        }
    }
}