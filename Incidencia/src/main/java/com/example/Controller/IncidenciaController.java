package com.example.Controller;

import com.example.Model.IncidenciaModel;
import com.example.Model.TecnicoModel;
import com.example.Service.IncidenciaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.ExampleObject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/incidencias")
@Tag(name = "Incidencias", description = "API para la gestión completa de incidencias técnicas")
public class IncidenciaController {

    @Autowired
    private IncidenciaService incidenciaService;

    @GetMapping
    @Operation(
        summary = "Obtener todas las incidencias", 
        description = "Retorna una lista completa de todas las incidencias registradas en el sistema, incluyendo información del usuario, equipo y técnico asignado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de incidencias obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<IncidenciaModel>> getAllIncidencias() {
        List<IncidenciaModel> incidencias = incidenciaService.findAll();
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener incidencia por ID", 
        description = "Retorna una incidencia específica basada en su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Incidencia encontrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = IncidenciaModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Incidencia no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"error\": \"Incidencia no encontrada\", \"mensaje\": \"No existe una incidencia con el ID proporcionado\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "ID inválido proporcionado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<IncidenciaModel> getIncidenciaById(
            @Parameter(
                description = "Identificador único de la incidencia", 
                required = true, 
                example = "1",
                schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id) {
        return incidenciaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(
        summary = "Obtener incidencias por cliente", 
        description = "Retorna todas las incidencias asociadas a un cliente específico, ordenadas por fecha de reporte"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de incidencias del cliente obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "ID de cliente inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<IncidenciaModel>> getIncidenciasByCliente(
            @Parameter(
                description = "Identificador único del cliente", 
                required = true, 
                example = "1",
                schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long idCliente) {
        List<IncidenciaModel> incidencias = incidenciaService.findByCliente(idCliente);
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/equipo/{idEquipo}")
    @Operation(
        summary = "Obtener incidencias por equipo", 
        description = "Retorna todas las incidencias asociadas a un equipo específico para análisis de fallos recurrentes"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de incidencias del equipo obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "ID de equipo inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<IncidenciaModel>> getIncidenciasByEquipo(
            @Parameter(
                description = "Identificador único del equipo", 
                required = true, 
                example = "1",
                schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long idEquipo) {
        List<IncidenciaModel> incidencias = incidenciaService.findByEquipo(idEquipo);
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/tecnico/{idTecnico}")
    @Operation(
        summary = "Obtener incidencias por técnico", 
        description = "Retorna todas las incidencias asignadas a un técnico específico para seguimiento de carga de trabajo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de incidencias del técnico obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "ID de técnico inválido",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<IncidenciaModel>> getIncidenciasByTecnico(
            @Parameter(
                description = "Identificador único del técnico", 
                required = true, 
                example = "1",
                schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long idTecnico) {
        List<IncidenciaModel> incidencias = incidenciaService.findByTecnico(idTecnico);
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/fecha")
    @Operation(
        summary = "Obtener incidencias por rango de fechas", 
        description = "Retorna incidencias filtradas por un rango de fechas de reporte específico para análisis estadístico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de incidencias en el rango de fechas obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Formato de fecha inválido o rango de fechas incorrecto",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"error\": \"Formato de fecha inválido\", \"mensaje\": \"Use el formato yyyy-MM-dd\"}")
            )
        )
    })
    public ResponseEntity<List<IncidenciaModel>> getIncidenciasByFechaReporte(
            @Parameter(
                description = "Fecha de inicio del rango de búsqueda", 
                required = true, 
                example = "2024-01-01",
                schema = @Schema(type = "string", format = "date")
            )
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @Parameter(
                description = "Fecha de fin del rango de búsqueda", 
                required = true, 
                example = "2024-12-31",
                schema = @Schema(type = "string", format = "date")
            )
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fin) {
        
        if (inicio.after(fin)) {
            return ResponseEntity.badRequest().build();
        }
        
        List<IncidenciaModel> incidencias = incidenciaService.findByFechaReporte(inicio, fin);
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/estado/{estado}")
    @Operation(
        summary = "Obtener incidencias por estado", 
        description = "Retorna incidencias filtradas por estado específico para seguimiento del flujo de trabajo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de incidencias por estado obtenida exitosamente",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Estado inválido proporcionado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<List<IncidenciaModel>> getIncidenciasByEstado(
            @Parameter(
                description = "Estado de la incidencia", 
                required = true, 
                example = "ABIERTA",
                schema = @Schema(
                    type = "string",
                    allowableValues = {"ABIERTA", "EN_PROCESO", "CERRADA", "CANCELADA"}
                )
            )
            @PathVariable String estado) {
        List<IncidenciaModel> incidencias = incidenciaService.findByEstado(estado);
        return ResponseEntity.ok(incidencias);
    }

    @PostMapping
    @Operation(
        summary = "Crear nueva incidencia", 
        description = "Crea una nueva incidencia en el sistema con validación de datos completa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Incidencia creada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = IncidenciaModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = "{\"error\": \"Datos inválidos\", \"mensaje\": \"La descripción es requerida\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<IncidenciaModel> createIncidencia(
            @Parameter(
                description = "Datos de la nueva incidencia", 
                required = true,
                content = @Content(
                    schema = @Schema(implementation = IncidenciaModel.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                                "  \"usuario\": {\"idusuario\": 1},\n" +
                                "  \"equipo\": {\"id\": 1},\n" +
                                "  \"descripcion\": \"Problema con la impresora\",\n" +
                                "  \"prioridad\": \"ALTA\"\n" +
                                "}"
                    )
                )
            )
            @Valid @RequestBody IncidenciaModel incidencia) {
        IncidenciaModel nuevaIncidencia = incidenciaService.save(incidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaIncidencia);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar incidencia completa", 
        description = "Actualiza todos los campos de una incidencia existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Incidencia actualizada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = IncidenciaModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Incidencia no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<IncidenciaModel> updateIncidencia(
            @Parameter(
                description = "Identificador único de la incidencia", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                description = "Datos actualizados de la incidencia", 
                required = true
            )
            @Valid @RequestBody IncidenciaModel incidencia) {
        return incidenciaService.findById(id)
                .map(existingIncidencia -> {
                    incidencia.setIdIncidencia(id);
                    IncidenciaModel updatedIncidencia = incidenciaService.save(incidencia);
                    return ResponseEntity.ok(updatedIncidencia);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar incidencia", 
        description = "Elimina permanentemente una incidencia del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "Incidencia eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Incidencia no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<Void> deleteIncidencia(
            @Parameter(
                description = "Identificador único de la incidencia", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id) {
        return incidenciaService.findById(id)
                .map(incidencia -> {
                    incidenciaService.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/asignar-tecnico")
    @Operation(
        summary = "Asignar técnico a incidencia", 
        description = "Asigna un técnico específico a una incidencia y cambia su estado a EN_PROCESO automáticamente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Técnico asignado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = IncidenciaModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Incidencia no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos del técnico inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<IncidenciaModel> asignarTecnico(
            @Parameter(
                description = "Identificador único de la incidencia", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                description = "Datos del técnico a asignar", 
                required = true,
                content = @Content(
                    schema = @Schema(implementation = TecnicoModel.class),
                    examples = @ExampleObject(
                        value = "{\n" +
                                "  \"idTecnico\": 1,\n" +
                                "  \"especialidad\": \"Reparación de impresoras\",\n" +
                                "  \"disponibilidad\": true\n" +
                                "}"
                    )
                )
            )
            @Valid @RequestBody TecnicoModel tecnico) {
        try {
            IncidenciaModel incidencia = incidenciaService.asignarTecnico(id, tecnico);
            return ResponseEntity.ok(incidencia);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cerrar")
    @Operation(
        summary = "Cerrar incidencia", 
        description = "Cierra una incidencia proporcionando la descripción de la solución aplicada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Incidencia cerrada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = IncidenciaModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Incidencia no encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Solución requerida",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<IncidenciaModel> cerrarIncidencia(
            @Parameter(
                description = "Identificador único de la incidencia", 
                required = true, 
                example = "1"
            )
            @PathVariable Long id,
            @Parameter(
                description = "Descripción detallada de la solución aplicada", 
                required = true,
                example = "Se reemplazó el cartucho de tinta y se calibró la impresora"
            )
            @RequestParam String solucion) {
        try {
            IncidenciaModel incidencia = incidenciaService.cerrarIncidencia(id, solucion);
            return ResponseEntity.ok(incidencia);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/pendientes")
    @Operation(
        summary = "Obtener incidencias pendientes", 
        description = "Retorna todas las incidencias con estado 'ABIERTA' ordenadas por prioridad"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Lista de incidencias pendientes obtenida exitosamente",
        content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = IncidenciaModel.class))
        )
    )
    public ResponseEntity<List<IncidenciaModel>> getIncidenciasPendientes() {
        List<IncidenciaModel> incidencias = incidenciaService.findIncidenciasPendientes();
        return ResponseEntity.ok(incidencias);
    }

    @GetMapping("/cliente/{idCliente}/count")
    @Operation(
        summary = "Contar incidencias por cliente", 
        description = "Retorna el número total de incidencias registradas por un cliente específico"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Número de incidencias del cliente obtenido exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(type = "integer", format = "int64", example = "15")
        )
    )
    public ResponseEntity<Long> countIncidenciasByCliente(
            @Parameter(
                description = "Identificador único del cliente", 
                required = true, 
                example = "1"
            )
            @PathVariable Long idCliente) {
        Long count = incidenciaService.countIncidenciasByUsuario(idCliente);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/estadisticas")
    @Operation(
        summary = "Obtener estadísticas de incidencias", 
        description = "Retorna estadísticas generales sobre el estado de las incidencias"
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Estadísticas obtenidas exitosamente",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EstadisticasResponse.class)
        )
    )
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();
        
        List<IncidenciaModel> todas = incidenciaService.findAll();
        estadisticas.put("total", todas.size());
        
        long abiertas = todas.stream().filter(i -> "ABIERTA".equals(i.getEstado())).count();
        long enProceso = todas.stream().filter(i -> "EN_PROCESO".equals(i.getEstado())).count();
        long cerradas = todas.stream().filter(i -> "CERRADA".equals(i.getEstado())).count();
        
        estadisticas.put("abiertas", abiertas);
        estadisticas.put("en_proceso", enProceso);
        estadisticas.put("cerradas", cerradas);
        
        return ResponseEntity.ok(estadisticas);
    }

    // Clases auxiliares para la documentación
    @Schema(description = "Respuesta de error estándar")
    public static class ErrorResponse {
        @Schema(description = "Código de error", example = "INCIDENCIA_NO_ENCONTRADA")
        private String error;
        
        @Schema(description = "Mensaje descriptivo del error", example = "No se encontró la incidencia con el ID especificado")
        private String mensaje;
        
        @Schema(description = "Timestamp del error", example = "2024-01-15T10:30:00Z")
        private String timestamp;

        // Constructores, getters y setters
        public ErrorResponse() {}

        public ErrorResponse(String error, String mensaje) {
            this.error = error;
            this.mensaje = mensaje;
            this.timestamp = new Date().toString();
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }

    @Schema(description = "Estadísticas de incidencias")
    public static class EstadisticasResponse {
        @Schema(description = "Total de incidencias", example = "150")
        private Long total;
        
        @Schema(description = "Incidencias abiertas", example = "25")
        private Long abiertas;
        
        @Schema(description = "Incidencias en proceso", example = "50")
        private Long enProceso;
        
        @Schema(description = "Incidencias cerradas", example = "75")
        private Long cerradas;

        // Constructores, getters y setters
        public EstadisticasResponse() {}

        public Long getTotal() { return total; }
        public void setTotal(Long total) { this.total = total; }
        public Long getAbiertas() { return abiertas; }
        public void setAbiertas(Long abiertas) { this.abiertas = abiertas; }
        public Long getEnProceso() { return enProceso; }
        public void setEnProceso(Long enProceso) { this.enProceso = enProceso; }
        public Long getCerradas() { return cerradas; }
        public void setCerradas(Long cerradas) { this.cerradas = cerradas; }
    }
}