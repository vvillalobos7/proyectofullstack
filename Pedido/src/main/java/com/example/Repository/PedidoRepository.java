package com.example.Repository;

import com.example.Model.PedidoModel;
import com.example.Model.PedidoModel.EstadoPedido;
import com.example.Model.PedidoModel.PrioridadPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {
    
    List<PedidoModel> findByClienteIdCliente(Long idCliente);
    
    List<PedidoModel> findByTecnicoIdTecnico(Long idTecnico);
    
    List<PedidoModel> findByEstado(EstadoPedido estado);
    
    List<PedidoModel> findByPrioridad(PrioridadPedido prioridad);
    
    List<PedidoModel> findByTipoServicio(String tipoServicio);
    
    List<PedidoModel> findByEstadoOrderByFechaCreacionAsc(EstadoPedido estado);
    
    List<PedidoModel> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<PedidoModel> findByFechaProgramadaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<PedidoModel> findByTecnicoIsNull();
    
    List<PedidoModel> findByEstadoAndPrioridadOrderByFechaCreacionAsc(EstadoPedido estado, PrioridadPedido prioridad);
    
    long countByEstado(EstadoPedido estado);
    
    long countByTecnicoIdTecnico(Long idTecnico);
    
    @Query("SELECT p FROM PedidoModel p WHERE p.prioridad = :prioridadUrgente OR p.prioridad = :prioridadAlta AND p.estado IN (:estadoPendiente, :estadoAsignado) ORDER BY p.prioridad DESC, p.fechaCreacion ASC")
    List<PedidoModel> findPedidosUrgentes(@Param("prioridadUrgente") PrioridadPedido prioridadUrgente, 
                                         @Param("prioridadAlta") PrioridadPedido prioridadAlta,
                                         @Param("estadoPendiente") EstadoPedido estadoPendiente,
                                         @Param("estadoAsignado") EstadoPedido estadoAsignado);
    
    List<PedidoModel> findByTecnicoIdTecnicoAndEstado(Long idTecnico, EstadoPedido estado);
     
    @Query("SELECT p FROM PedidoModel p WHERE p.estado = :estadoCompletado AND p.fechaCompletado BETWEEN :fechaInicio AND :fechaFin")
    List<PedidoModel> findPedidosCompletadosEnRango(@Param("estadoCompletado") EstadoPedido estadoCompletado,
                                                   @Param("fechaInicio") LocalDateTime fechaInicio, 
                                                   @Param("fechaFin") LocalDateTime fechaFin);
}