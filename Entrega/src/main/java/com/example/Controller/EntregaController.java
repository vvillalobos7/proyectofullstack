package com.example.Controller;

import com.example.Model.EntregaModel;
import com.example.Service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/entregas")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @Operation(summary = "Obtener todas las entregas", description = "Recupera una lista de todas las entregas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entregas obtenidas exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<EntregaModel>> getAllEntregas() {
        List<EntregaModel> entregas = entregaService.findAll();
        return ResponseEntity.ok(entregas);
    }

    @Operation(summary = "Obtener una entrega por ID", description = "Obtiene una entrega específica usando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega encontrada"),
        @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntregaModel> getEntregaById(@PathVariable Long id) {
        try {
            EntregaModel entrega = entregaService.findById(id);
            return ResponseEntity.ok(entrega);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Crear una nueva entrega", description = "Crea una nueva entrega en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Entrega creada exitosamente")
    })
    @PostMapping
    public ResponseEntity<EntregaModel> createEntrega(@RequestBody EntregaModel entrega) {
        EntregaModel createdEntrega = entregaService.save(entrega);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntrega);
    }

    @Operation(summary = "Actualizar una entrega", description = "Actualiza los detalles de una entrega existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntregaModel> updateEntrega(@PathVariable Long id, @RequestBody EntregaModel entrega) {
        try {
            EntregaModel updatedEntrega = entregaService.update(id, entrega);
            return ResponseEntity.ok(updatedEntrega);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar una entrega", description = "Elimina una entrega usando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Entrega eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Entrega no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntrega(@PathVariable Long id) {
        try {
            entregaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Asignar transportista a una entrega", description = "Asigna un transportista y su vehículo a una entrega específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transportista asignado correctamente")
    })
    @PutMapping("/{id}/asignar-transportista")
    public ResponseEntity<EntregaModel> asignarTransportista(@PathVariable Long id,
                                                             @RequestParam String transportista,
                                                             @RequestParam String vehiculo,
                                                             @RequestParam String patente) {
        EntregaModel updatedEntrega = entregaService.asignarTransportista(id, transportista, vehiculo, patente);
        return ResponseEntity.ok(updatedEntrega);
    }

    @Operation(summary = "Cambiar el estado de una entrega", description = "Cambia el estado de la entrega (por ejemplo, 'EN_TRANSITO')")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado de la entrega actualizado"),
        @ApiResponse(responseCode = "400", description = "Estado no válido")
    })
    @PutMapping("/{id}/cambiar-estado")
    public ResponseEntity<EntregaModel> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            EntregaModel updatedEntrega = entregaService.cambiarEstado(id, estado);
            return ResponseEntity.ok(updatedEntrega);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Iniciar transporte de la entrega", description = "Cambia el estado de la entrega a 'EN_TRANSITO'")
    @PutMapping("/{id}/iniciar-transporte")
    public ResponseEntity<EntregaModel> iniciarTransporte(@PathVariable Long id) {
        EntregaModel updatedEntrega = entregaService.cambiarEstado(id, "EN_TRANSITO");
        return ResponseEntity.ok(updatedEntrega);
    }

    @Operation(summary = "Completar entrega", description = "Marca la entrega como completada y actualiza los kilómetros recorridos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega completada exitosamente")
    })
    @PutMapping("/{id}/completar")
    public ResponseEntity<EntregaModel> completarEntrega(@PathVariable Long id,
                                                        @RequestParam int kmRecorridos) {
        EntregaModel updatedEntrega = entregaService.completarEntrega(id, kmRecorridos);
        return ResponseEntity.ok(updatedEntrega);
    }

    @Operation(summary = "Cancelar entrega", description = "Cancela la entrega y proporciona el motivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entrega cancelada correctamente")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<EntregaModel> cancelarEntrega(@PathVariable Long id,
                                                       @RequestParam String motivo) {
        EntregaModel updatedEntrega = entregaService.cancelarEntrega(id, motivo);
        return ResponseEntity.ok(updatedEntrega);
    }

    @Operation(summary = "Obtener ciudades disponibles", description = "Recupera la lista de ciudades a las que se puede realizar una entrega")
    @GetMapping("/ciudades")
    public ResponseEntity<List<String>> getCiudadesDisponibles() {
        List<String> ciudades = entregaService.getCiudadesDisponibles();
        return ResponseEntity.ok(ciudades);
    }

    @Operation(summary = "Obtener transportistas disponibles", description = "Recupera la lista de transportistas disponibles para las entregas")
    @GetMapping("/transportistas")
    public ResponseEntity<List<String>> getTransportistasDisponibles() {
        List<String> transportistas = entregaService.getTransportistasDisponibles();
        return ResponseEntity.ok(transportistas);
    }

    @Operation(summary = "Contar entregas por estado", description = "Cuenta el número de entregas con un estado específico")
    @GetMapping("/contar/{estado}")
    public ResponseEntity<String> contarPorEstado(@PathVariable String estado) {
        long count = entregaService.contarPorEstado(estado);
        return ResponseEntity.ok(String.valueOf(count));
    }

    @Operation(summary = "Validar disponibilidad de transportista", description = "Valida si un transportista está disponible para una fecha específica")
    @GetMapping("/validar-transportista")
    public ResponseEntity<String> validarDisponibilidadTransportista(@RequestParam String transportista,
                                                                    @RequestParam String fecha) {
        LocalDateTime fechaRequest = LocalDateTime.parse(fecha);
        boolean disponible = entregaService.validarDisponibilidadTransportista(transportista, fechaRequest);
        return ResponseEntity.ok(String.valueOf(disponible));
    }

    @Operation(summary = "Obtener costo total de entregas por periodo", description = "Calcula el costo total de las entregas en un periodo determinado")
    @GetMapping("/reportes/costo-total")
    public ResponseEntity<String> getTotalCostoEntregas(@RequestParam String inicio,
                                                        @RequestParam String fin) {
        LocalDateTime inicioDate = LocalDateTime.parse(inicio);
        LocalDateTime finDate = LocalDateTime.parse(fin);
        double totalCosto = entregaService.getTotalCostoEntregasPorPeriodo(inicioDate, finDate);
        return ResponseEntity.ok(String.valueOf(totalCosto));
    }

    @Operation(summary = "Obtener entregas por ciudad y periodo", description = "Recupera las entregas realizadas en una ciudad y en un periodo de tiempo específico")
    @GetMapping("/reportes/por-ciudad")
    public ResponseEntity<List<EntregaModel>> getEntregasPorCiudadYPeriodo(@RequestParam String ciudad,
                                                                           @RequestParam String inicio,
                                                                           @RequestParam String fin) {
        LocalDateTime inicioDate = LocalDateTime.parse(inicio);
        LocalDateTime finDate = LocalDateTime.parse(fin);
        List<EntregaModel> entregas = entregaService.findEntregasPorCiudadYPeriodo(ciudad, inicioDate, finDate);
        return ResponseEntity.ok(entregas);
    }
}