package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "mantenimiento")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de Mantenimiento programado")
public class MantenimientoModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del mantenimiento", example = "1")
    private Long idMantenimiento;
    
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    @Schema(description = "Pedido asociado al mantenimiento")
    private PedidoModel pedido;
    
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    @Schema(description = "Equipo que recibirá el mantenimiento")
    private EquipoModel equipo;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha programada para el mantenimiento", 
            example = "2024-01-20T14:00:00.000Z", 
            required = true)
    private Date fechaProgramada;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Fecha en que se realizó el mantenimiento", 
            example = "2024-01-20T16:30:00.000Z")
    private Date fechaRealizada;
    
    @Column(nullable = false)
    @Schema(description = "Tipo de mantenimiento", 
            example = "PREVENTIVO", 
            allowableValues = {"PREVENTIVO", "CORRECTIVO", "PREDICTIVO", "EMERGENCIA"}, 
            required = true)
    private String tipo;
    
    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    @Schema(description = "Técnico responsable del mantenimiento")
    private TecnicoModel tecnico;
}