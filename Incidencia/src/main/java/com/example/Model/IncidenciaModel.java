package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "incidencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de Incidencia reportada")
public class IncidenciaModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la incidencia", example = "1")
    private Long idIncidencia;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario que reporta la incidencia")
    private UsuarioModel usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    @Schema(description = "Equipo afectado por la incidencia")
    private EquipoModel equipo;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha en que se reportó la incidencia", 
            example = "2024-01-15T10:30:00.000Z", 
            required = true)
    private Date fechaReporte;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha de creación del registro", example = "2024-01-15T10:30:00.000Z")
    private Date fechaCreacion;
    
    @Column(nullable = false, length = 1000)
    @Schema(description = "Descripción detallada de la incidencia", 
            example = "El compresor hace ruidos extraños y pierde presión", 
            required = true, 
            maxLength = 1000)
    private String descripcion;
    
    @Column(length = 50)
    @Schema(description = "Estado actual de la incidencia", 
            example = "ABIERTA", 
            allowableValues = {"ABIERTA", "EN_PROCESO", "RESUELTA", "CERRADA"}, 
            defaultValue = "ABIERTA")
    private String estado = "ABIERTA";
    
    @Column(length = 50)
    @Schema(description = "Prioridad de la incidencia", 
            example = "MEDIA", 
            allowableValues = {"BAJA", "MEDIA", "ALTA", "CRITICA"}, 
            defaultValue = "MEDIA")
    private String prioridad = "MEDIA";
    
    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    @Schema(description = "Técnico asignado para resolver la incidencia")
    private TecnicoModel tecnico;
    
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha en que se resolvió la incidencia", example = "2024-01-16T14:30:00.000Z")
    private Date fechaResolucion;
    
    @Column(length = 1000)
    @Schema(description = "Descripción de la solución aplicada", 
            example = "Se reemplazó el filtro de aire y se ajustó la presión", 
            maxLength = 1000)
    private String solucion;
    
    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = new Date();
        }
        if (this.fechaReporte == null) {
            this.fechaReporte = new Date();
        }
    }
}