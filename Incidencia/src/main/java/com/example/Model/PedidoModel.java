package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Modelo de Pedido de servicio")
public class PedidoModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pedido", example = "1")
    private Long idPedido;
    
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    @Schema(description = "Cliente que solicita el servicio")
    private UsuarioModel cliente;
    
    @ManyToOne
    @JoinColumn(name = "id_tecnico")
    @Schema(description = "Técnico asignado al pedido")
    private TecnicoModel tecnico;
    
    @Column(name = "descripcion_servicio", nullable = false, length = 500)
    @Schema(description = "Descripción detallada del servicio solicitado", 
            example = "Mantenimiento preventivo de compresor industrial", 
            required = true, 
            maxLength = 500)
    private String descripcionServicio;
    
    @Column(name = "tipo_servicio", nullable = false)
    @Schema(description = "Tipo de servicio solicitado", 
            example = "MANTENIMIENTO", 
            allowableValues = {"MANTENIMIENTO", "REPARACION", "INSTALACION", "INSPECCION"})
    private String tipoServicio;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Schema(description = "Estado actual del pedido", example = "PENDIENTE")
    private EstadoPedido estado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad")
    @Schema(description = "Prioridad del pedido", example = "MEDIA")
    private PrioridadPedido prioridad;
    
    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha de creación del pedido", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_programada")
    @Schema(description = "Fecha programada para el servicio", example = "2024-01-20T14:00:00")
    private LocalDateTime fechaProgramada;
    
    @Column(name = "fecha_completado")
    @Schema(description = "Fecha de completado del servicio", example = "2024-01-20T16:30:00")
    private LocalDateTime fechaCompletado;
    
    @Column(name = "direccion_servicio", nullable = false)
    @Schema(description = "Dirección donde se realizará el servicio", 
            example = "Av. Libertador 1234, Santiago", 
            required = true)
    private String direccionServicio;
    
    @Column(name = "telefono_contacto")
    @Schema(description = "Teléfono de contacto para el servicio", example = "+56912345678")
    private String telefonoContacto;
    
    @Column(name = "costo_estimado", precision = 10, scale = 2)
    @Schema(description = "Costo estimado del servicio", example = "150000.00")
    private BigDecimal costoEstimado;
    
    @Column(name = "costo_final", precision = 10, scale = 2)
    @Schema(description = "Costo final del servicio", example = "165000.00")
    private BigDecimal costoFinal;
    
    @Column(name = "observaciones", length = 1000)
    @Schema(description = "Observaciones adicionales del pedido", 
            example = "Cliente prefiere horario matutino", 
            maxLength = 1000)
    private String observaciones;
    
    @Column(name = "materiales_necesarios", length = 500)
    @Schema(description = "Materiales necesarios para el servicio", 
            example = "Filtros de aire, aceite hidráulico", 
            maxLength = 500)
    private String materialesNecesarios;
    
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoPedido.PENDIENTE;
        }
        if (this.prioridad == null) {
            this.prioridad = PrioridadPedido.MEDIA;
        }
    }
    
    @Schema(description = "Estados posibles de un pedido")
    public enum EstadoPedido {
        @Schema(description = "Pedido recién creado, esperando asignación")
        PENDIENTE,
        @Schema(description = "Pedido asignado a un técnico")
        ASIGNADO,
        @Schema(description = "Servicio en proceso de ejecución")
        EN_PROCESO,
        @Schema(description = "Servicio pausado temporalmente")
        PAUSADO,
        @Schema(description = "Servicio completado exitosamente")
        COMPLETADO,
        @Schema(description = "Pedido cancelado")
        CANCELADO
    }
    
    @Schema(description = "Niveles de prioridad para pedidos")
    public enum PrioridadPedido {
        @Schema(description = "Prioridad baja, no urgente")
        BAJA,
        @Schema(description = "Prioridad media, estándar")
        MEDIA,
        @Schema(description = "Prioridad alta, requiere atención pronta")
        ALTA,
        @Schema(description = "Prioridad urgente, atención inmediata")
        URGENTE
    }
}