package com.example.Controller;

import com.example.Model.InventarioModel;
import com.example.Service.InventarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventario")
@Tag(name = "Inventario", description = "API para gestión de inventario de equipos")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(summary = "Obtener todos los inventarios", description = "Retorna una lista de todos los registros de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping
    public ResponseEntity<List<InventarioModel>> getAllInventarios() {
        return ResponseEntity.ok(inventarioService.findAll());
    }

    @Operation(summary = "Obtener inventario por ID", description = "Retorna un registro de inventario específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrado",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioModel> getInventarioById(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id) {
        try {
            InventarioModel inventario = inventarioService.findById(id);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener inventario por equipo", description = "Retorna el inventario de un equipo específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario encontrado",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado para el equipo")
    })
    @GetMapping("/equipo/{idEquipo}")
    public ResponseEntity<InventarioModel> getInventarioByEquipo(
            @Parameter(description = "ID del equipo", required = true) @PathVariable Long idEquipo) {
        Optional<InventarioModel> inventario = inventarioService.findByEquipo(idEquipo);
        return inventario.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener inventarios por estado", description = "Retorna inventarios filtrados por estado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<InventarioModel>> getInventariosByEstado(
            @Parameter(description = "Estado del inventario (DISPONIBLE, CRITICO, AGOTADO)", required = true) 
            @PathVariable String estado) {
        return ResponseEntity.ok(inventarioService.findByEstado(estado));
    }

    @Operation(summary = "Obtener inventarios por ubicación", description = "Retorna inventarios filtrados por ubicación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<InventarioModel>> getInventariosByUbicacion(
            @Parameter(description = "Ubicación del inventario", required = true) @PathVariable String ubicacion) {
        return ResponseEntity.ok(inventarioService.findByUbicacion(ubicacion));
    }

    @Operation(summary = "Obtener stock crítico", description = "Retorna inventarios con stock crítico (menor o igual al mínimo)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping("/stock-critico")
    public ResponseEntity<List<InventarioModel>> getStockCritico() {
        return ResponseEntity.ok(inventarioService.findStockCritico());
    }

    @Operation(summary = "Obtener stock agotado", description = "Retorna inventarios con stock agotado (cantidad = 0)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping("/stock-agotado")
    public ResponseEntity<List<InventarioModel>> getStockAgotado() {
        return ResponseEntity.ok(inventarioService.findStockAgotado());
    }

    @Operation(summary = "Obtener inventarios por tipo de equipo", description = "Retorna inventarios filtrados por tipo de equipo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping("/tipo-equipo/{tipoEquipo}")
    public ResponseEntity<List<InventarioModel>> getInventariosByTipoEquipo(
            @Parameter(description = "Tipo de equipo (TRACTOR, COSECHADORA, SEMBRADORA, etc.)", required = true) 
            @PathVariable String tipoEquipo) {
        return ResponseEntity.ok(inventarioService.findByTipoEquipo(tipoEquipo));
    }

    @Operation(summary = "Obtener equipos disponibles por cantidad", description = "Retorna inventarios con stock disponible mayor o igual a la cantidad especificada")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class)))
    })
    @GetMapping("/disponibles/{cantidad}")
    public ResponseEntity<List<InventarioModel>> getDisponiblesPorCantidad(
            @Parameter(description = "Cantidad mínima disponible", required = true) @PathVariable Integer cantidad) {
        return ResponseEntity.ok(inventarioService.findDisponiblesPorCantidad(cantidad));
    }

    @Operation(summary = "Crear nuevo inventario", description = "Crea un nuevo registro de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Inventario creado exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<InventarioModel> createInventario(
            @Parameter(description = "Datos del inventario a crear", required = true) 
            @Valid @RequestBody InventarioModel inventario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.save(inventario));
    }

    @Operation(summary = "Actualizar inventario", description = "Actualiza un registro de inventario existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InventarioModel> updateInventario(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id,
            @Parameter(description = "Datos actualizados del inventario", required = true) 
            @Valid @RequestBody InventarioModel inventario) {
        try {
            InventarioModel updatedInventario = inventarioService.update(id, inventario);
            return ResponseEntity.ok(updatedInventario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar inventario", description = "Elimina un registro de inventario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inventario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventario(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id) {
        try {
            inventarioService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Aumentar stock", description = "Incrementa el stock disponible y total")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock aumentado exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "400", description = "Error al aumentar stock"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}/aumentar-stock")
    public ResponseEntity<InventarioModel> aumentarStock(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id,
            @Parameter(description = "Cantidad a aumentar", required = true) @RequestParam Integer cantidad) {
        try {
            InventarioModel inventario = inventarioService.aumentarStock(id, cantidad);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Reducir stock", description = "Reduce el stock disponible y aumenta el arrendado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock reducido exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente o error"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}/reducir-stock")
    public ResponseEntity<InventarioModel> reducirStock(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id,
            @Parameter(description = "Cantidad a reducir", required = true) @RequestParam Integer cantidad) {
        try {
            InventarioModel inventario = inventarioService.reducirStock(id, cantidad);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Devolver equipo", description = "Devuelve equipos arrendados al stock disponible")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Equipo devuelto exitosamente",
                    content = @Content(schema = @Schema(implementation = InventarioModel.class))),
        @ApiResponse(responseCode = "400", description = "Error al devolver equipo"),
        @ApiResponse(responseCode = "404", description = "Inventario no encontrado")
    })
    @PutMapping("/{id}/devolver-equipo")
    public ResponseEntity<InventarioModel> devolverEquipo(
            @Parameter(description = "ID del inventario", required = true) @PathVariable Long id,
            @Parameter(description = "Cantidad a devolver", required = true) @RequestParam Integer cantidad) {
        try {
            InventarioModel inventario = inventarioService.devolverEquipo(id, cantidad);
            return ResponseEntity.ok(inventario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Verificar disponibilidad", description = "Verifica si hay suficiente stock disponible")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    @GetMapping("/verificar-disponibilidad/{idEquipo}")
    public ResponseEntity<Boolean> verificarDisponibilidad(
            @Parameter(description = "ID del equipo", required = true) @PathVariable Long idEquipo,
            @Parameter(description = "Cantidad requerida", required = true) @RequestParam Integer cantidad) {
        boolean disponible = inventarioService.verificarDisponibilidad(idEquipo, cantidad);
        return ResponseEntity.ok(disponible);
    }

    @Operation(summary = "Obtener stock total", description = "Retorna la suma total de stock en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total obtenido exitosamente")
    })
    @GetMapping("/reportes/total-stock")
    public ResponseEntity<Long> getTotalStock() {
        return ResponseEntity.ok(inventarioService.getTotalStock());
    }

    @Operation(summary = "Obtener stock disponible total", description = "Retorna la suma total de stock disponible")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total obtenido exitosamente")
    })
    @GetMapping("/reportes/stock-disponible")
    public ResponseEntity<Long> getTotalStockDisponible() {
        return ResponseEntity.ok(inventarioService.getTotalStockDisponible());
    }

    @Operation(summary = "Obtener stock arrendado total", description = "Retorna la suma total de stock arrendado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total obtenido exitosamente")
    })
    @GetMapping("/reportes/stock-arrendado")
    public ResponseEntity<Long> getTotalStockArrendado() {
        return ResponseEntity.ok(inventarioService.getTotalStockArrendado());
    }
}