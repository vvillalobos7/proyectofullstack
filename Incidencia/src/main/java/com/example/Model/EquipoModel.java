package com.example.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "equipos")
@Schema(description = "Modelo que representa un equipo en el sistema")
public class EquipoModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del equipo", example = "1")
    private Long idEquipo;
    
    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Schema(description = "Nombre del equipo", example = "Tractor Case I.H. 4210A", required = true)
    private String nombre;
    
    @Schema(description = "Descripción detallada del equipo", example = "Tractor")
    private String descripcion;
    
    @NotBlank(message = "La marca es obligatoria")
    @Schema(description = "Marca del equipo", example = "Case", required = true)
    private String marca;
    
    @Schema(description = "Modelo específico del equipo", example = "4210A")
    private String modelo;
    
    @NotBlank(message = "El estado es obligatorio")
    @Schema(description = "Estado del equipo", example = "NUEVO", allowableValues = {"NUEVO", "USADO", "REPARACION", "DAÑADO"})
    private String estado;
    
    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Schema(description = "Precio de venta del equipo", example = "999.99", required = true)
    private Double precioventa;
    
    @Schema(description = "Patente o número de serie del equipo", example = "ABC123")
    private String patente;
    
    @NotBlank(message = "El tipo de equipo es obligatorio")
    @Schema(description = "Tipo de equipo", example = "Tractor", allowableValues = {"TRACTOR", "COSECHADORA", "SEMBRADORA", "ARADO", "RASTRILLO", "OTRO"})
    private String tipoEquipo;
}