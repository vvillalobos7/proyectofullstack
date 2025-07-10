package com.example.Seguimiento.Controller;

import com.example.Seguimiento.Model.Seguimiento;
import com.example.Seguimiento.Service.SeguimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seguimiento")
@Tag(name = "Seguimientos", description = "API para gestión de seguimientos de entregas")
public class SeguimientoController {

    private final SeguimientoService seguimientoService;

    public SeguimientoController(SeguimientoService seguimientoService) {
        this.seguimientoService = seguimientoService;
    }

    @Operation(summary = "Listar todos los seguimientos", 
               description = "Este endpoint devuelve una lista de todos los seguimientos de entregas registrados en el sistema.")
    @GetMapping
    public ResponseEntity<List<Seguimiento>> listar() {
        return ResponseEntity.ok(seguimientoService.obtenerTodos());
    }

    @Operation(summary = "Crear un nuevo seguimiento",
               description = "Este endpoint permite la creación de un nuevo seguimiento para registrar el estado de una entrega.")
    @PostMapping
    public ResponseEntity<Seguimiento> crear(@RequestBody Seguimiento seguimiento) {
        Seguimiento creado = seguimientoService.guardar(seguimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Obtener un seguimiento por ID", 
               description = "Este endpoint devuelve un seguimiento específico a partir de su ID único.")
    @GetMapping("/{id}")
    public ResponseEntity<Seguimiento> obtener(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(seguimientoService.obtenerPorId(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Eliminar un seguimiento por ID", 
               description = "Este endpoint permite eliminar un seguimiento existente a partir de su ID único.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            seguimientoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}