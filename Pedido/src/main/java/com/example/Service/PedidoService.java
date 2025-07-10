package com.example.Service;

import com.example.Model.PedidoModel;
import com.example.Model.PedidoModel.EstadoPedido;
import com.example.Model.PedidoModel.PrioridadPedido;
import com.example.Repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public List<PedidoModel> getPedidos() {
        return pedidoRepository.findAll();
    }

    public PedidoModel getPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
    }

    public PedidoModel savePedido(PedidoModel pedido) {
        return pedidoRepository.save(pedido);
    }

    public PedidoModel updatePedido(Long id, PedidoModel pedidoActualizado) {
        PedidoModel existente = getPedidoPorId(id);
        pedidoActualizado.setIdPedido(id);
        return pedidoRepository.save(pedidoActualizado);
    }

    public void deletePedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido no encontrado con ID: " + id + " para eliminar");
        }
        pedidoRepository.deleteById(id);
    }

    public PedidoModel iniciarTrabajo(Long id) {
        PedidoModel pedido = getPedidoPorId(id);
        pedido.setEstado(EstadoPedido.EN_PROCESO);
        return pedidoRepository.save(pedido);
    }

    public PedidoModel cancelarPedido(Long id) {
        PedidoModel pedido = getPedidoPorId(id);
        pedido.setEstado(EstadoPedido.CANCELADO);
        return pedidoRepository.save(pedido);
    }

    public PedidoModel cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        PedidoModel pedido = getPedidoPorId(id);
        pedido.setEstado(nuevoEstado);
        return pedidoRepository.save(pedido);
    }

    public long contarPedidosPorEstado(EstadoPedido estado) {
        return pedidoRepository.countByEstado(estado);
    }

    public List<PedidoModel> findPedidosCompletadosEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findPedidosCompletadosEnRango(EstadoPedido.COMPLETADO, fechaInicio, fechaFin);
    }

    public List<PedidoModel> findPedidosProgramados(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findByFechaProgramadaBetween(fechaInicio, fechaFin);
    }

    public List<PedidoModel> findByRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findByFechaCreacionBetween(fechaInicio, fechaFin);
    }

    public List<PedidoModel> findPedidosUrgentes() {
        return pedidoRepository.findPedidosUrgentes(
                PrioridadPedido.URGENTE,
                PrioridadPedido.ALTA,
                EstadoPedido.PENDIENTE,
                EstadoPedido.ASIGNADO
        );
    }

    public List<PedidoModel> findByTipoServicio(String tipoServicio) {
        return pedidoRepository.findByTipoServicio(tipoServicio);
    }

    public List<PedidoModel> findByPrioridad(PrioridadPedido prioridad) {
        return pedidoRepository.findByPrioridad(prioridad);
    }

    public List<PedidoModel> findByEstado(EstadoPedido estado) {
        return pedidoRepository.findByEstado(estado);
    }

    public List<PedidoModel> findByCliente(Long idCliente) {
        return pedidoRepository.findByClienteIdCliente(idCliente);
    }
}