package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "tecnico")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de Técnico especializado")
public class TecnicoModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del técnico", example = "1")
    private Long idTecnico;
    
    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @Schema(description = "Usuario asociado al técnico")
    private UsuarioModel usuario;
    
    @Column(nullable = false)
    @Schema(description = "Especialidad del técnico", example = "Electricidad Industrial", required = true)
    private String especialidad;
    
    @Schema(description = "Disponibilidad del técnico", example = "true")
    private Boolean disponibilidad;
    
    @Schema(description = "Zona de cobertura del técnico", example = "Región Metropolitana")
    private String zonaCobertura;
    
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Lista de mantenimientos asignados al técnico")
    private List<MantenimientoModel> mantenimientos;
    
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Lista de incidencias asignadas al técnico")
    private List<IncidenciaModel> incidencias;
}