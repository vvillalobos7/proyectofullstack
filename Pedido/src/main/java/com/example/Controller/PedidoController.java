package com.example.Controller;

import com.example.Model.PedidoModel;
import com.example.Model.PedidoModel.EstadoPedido;
import com.example.Model.PedidoModel.PrioridadPedido;
import com.example.Service.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Obtener todos los pedidos", description = "Retorna una lista de todos los pedidos registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<PedidoModel>> getAllPedidos() {
        return ResponseEntity.ok(pedidoService.getPedidos());
    }

    @Operation(summary = "Obtener pedido por ID", description = "Retorna el pedido correspondiente al ID dado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoModel> getPedidoById(
        @Parameter(description = "ID del pedido a buscar", example = "1")
        @PathVariable Long id) {
        try {
            PedidoModel pedido = pedidoService.getPedidoPorId(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crear un nuevo pedido", description = "Crea un nuevo pedido en el sistema")
    @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente")
    @PostMapping
    public ResponseEntity<PedidoModel> createPedido(@RequestBody PedidoModel pedido) {
        try {
            PedidoModel nuevoPedido = pedidoService.savePedido(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un pedido existente", description = "Actualiza los detalles de un pedido por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PedidoModel> updatePedido(@PathVariable Long id, @RequestBody PedidoModel pedido) {
        try {
            PedidoModel pedidoActualizado = pedidoService.updatePedido(id, pedido);
            return ResponseEntity.ok(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un pedido", description = "Elimina un pedido por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        try {
            pedidoService.deletePedido(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener pedidos por cliente", description = "Retorna los pedidos asociados a un cliente por ID")
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<?> getPedidosByCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(pedidoService.findByCliente(idCliente));
    }

    @Operation(summary = "Obtener pedidos por estado", description = "Retorna los pedidos de acuerdo al estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getPedidosByEstado(@PathVariable EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.findByEstado(estado));
    }

    @Operation(summary = "Obtener pedidos por prioridad", description = "Retorna los pedidos de acuerdo a la prioridad")
    @GetMapping("/prioridad/{prioridad}")
    public ResponseEntity<?> getPedidosByPrioridad(@PathVariable PrioridadPedido prioridad) {
        return ResponseEntity.ok(pedidoService.findByPrioridad(prioridad));
    }

    @Operation(summary = "Obtener pedidos por tipo de servicio", description = "Retorna los pedidos de acuerdo al tipo de servicio")
    @GetMapping("/tipo-servicio/{tipoServicio}")
    public ResponseEntity<?> getPedidosByTipoServicio(@PathVariable String tipoServicio) {
        return ResponseEntity.ok(pedidoService.findByTipoServicio(tipoServicio));
    }

    @Operation(summary = "Obtener pedidos urgentes", description = "Retorna los pedidos con prioridad urgente")
    @GetMapping("/urgentes")
    public ResponseEntity<?> getPedidosUrgentes() {
        return ResponseEntity.ok(pedidoService.findPedidosUrgentes());
    }

    @Operation(summary = "Obtener pedidos por rango de fechas", description = "Retorna los pedidos creados en un rango de fechas")
    @GetMapping("/rango-fechas")
    public ResponseEntity<?> getPedidosByRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pedidoService.findByRangoFechas(fechaInicio, fechaFin));
    }

    @Operation(summary = "Obtener pedidos programados", description = "Retorna los pedidos programados en un rango de fechas")
    @GetMapping("/programados")
    public ResponseEntity<?> getPedidosProgramados(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pedidoService.findPedidosProgramados(fechaInicio, fechaFin));
    }

    @Operation(summary = "Obtener pedidos completados en un rango de fechas", description = "Retorna los pedidos completados en un rango de fechas")
    @GetMapping("/completados/rango")
    public ResponseEntity<?> getPedidosCompletadosEnRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        return ResponseEntity.ok(pedidoService.findPedidosCompletadosEnRango(fechaInicio, fechaFin));
    }

    @Operation(summary = "Cambiar estado del pedido", description = "Permite cambiar el estado de un pedido por su ID")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            EstadoPedido nuevoEstado = EstadoPedido.valueOf(request.get("estado").toUpperCase());
            PedidoModel pedido = pedidoService.cambiarEstado(id, nuevoEstado);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Iniciar trabajo del pedido", description = "Marca el pedido como iniciado")
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<?> iniciarTrabajo(@PathVariable Long id) {
        try {
            PedidoModel pedido = pedidoService.iniciarTrabajo(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cancelar pedido", description = "Cancela el pedido y lo marca como cancelado")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {
        try {
            PedidoModel pedido = pedidoService.cancelarPedido(id);
            return ResponseEntity.ok(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Contar pedidos por estado", description = "Retorna la cantidad de pedidos en un estado específico")
    @GetMapping("/estadisticas/por-estado/{estado}")
    public ResponseEntity<?> contarPedidosPorEstado(@PathVariable EstadoPedido estado) {
        long cantidad = pedidoService.contarPedidosPorEstado(estado);
        return ResponseEntity.ok(cantidad);
    }
}