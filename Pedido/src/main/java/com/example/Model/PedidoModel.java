package com.example.Model;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Schema(description = "Entidad que representa un pedido en AgroTech")
public class PedidoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del pedido", example = "1")
    private Long idPedido;

    @Column(name = "descripcion_servicio", nullable = false, length = 500)
    @Schema(description = "Descripción del servicio solicitado", example = "Instalación de sistema de riego")
    private String descripcionServicio;

    @Column(name = "tipo_servicio", nullable = false)
    @Schema(description = "Tipo de servicio", example = "Instalación")
    private String tipoServicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @Schema(description = "Estado actual del pedido", example = "PENDIENTE")
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false)
    @Schema(description = "Prioridad del pedido", example = "MEDIA")
    private PrioridadPedido prioridad = PrioridadPedido.MEDIA;

    @Column(name = "fecha_creacion", nullable = false)
    @Schema(description = "Fecha de creación del pedido", example = "2025-07-09T12:30:00")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_programada")
    @Schema(description = "Fecha programada para ejecutar el pedido", example = "2025-07-15T09:00:00")
    private LocalDateTime fechaProgramada;

    @Column(name = "fecha_completado")
    @Schema(description = "Fecha en que se completó el pedido", example = "2025-07-20T15:00:00")
    private LocalDateTime fechaCompletado;

    @Column(name = "direccion_servicio", nullable = false)
    @Schema(description = "Dirección donde se realizará el servicio", example = "Av. Los Agricultores 123, Chillán")
    private String direccionServicio;

    @Column(name = "telefono_contacto", nullable = false)
    @Schema(description = "Número de contacto del cliente", example = "+56912345678")
    private String telefonoContacto;

    @Column(name = "costo_estimado", precision = 10, scale = 2)
    @Schema(description = "Costo estimado del servicio", example = "150000.00")
    private BigDecimal costoEstimado;

    @Column(name = "costo_final", precision = 10, scale = 2)
    @Schema(description = "Costo final del servicio", example = "155000.00")
    private BigDecimal costoFinal;

    @Column(name = "observaciones", length = 1000)
    @Schema(description = "Observaciones adicionales sobre el pedido", example = "Requiere aprobación del supervisor.")
    private String observaciones;

    @Column(name = "materiales_necesarios", length = 500)
    @Schema(description = "Lista de materiales necesarios", example = "Tuberías, aspersores, válvulas")
    private String materialesNecesarios;

    @PrePersist
    protected void onCreate() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
    }

    public enum EstadoPedido {
        PENDIENTE, ASIGNADO, EN_PROCESO, PAUSADO, COMPLETADO, CANCELADO
    }

    public enum PrioridadPedido {
        BAJA, MEDIA, ALTA, URGENTE
    }
}